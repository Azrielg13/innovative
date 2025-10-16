package com.digitald4.iis.storage;

import static com.digitald4.iis.server.Constants.NURSE_ID;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;
import static com.digitald4.iis.model.ServiceCode.Unit.Hour;
import static com.digitald4.iis.model.ServiceCode.Unit.Visit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.common.storage.Transaction.Op;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AppointmentState;
import com.digitald4.iis.model.User.Role;
import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class AppointmentStore extends GenericStore<Appointment, Long> {
  private final Provider<DAO> daoProvider;
  private final Provider<User> userProvider;
  private final ServiceCodeStore serviceCodeStore;
  private final Clock clock;

  @Inject
  public AppointmentStore(Provider<DAO> daoProvider, Provider<User> userProvider, ServiceCodeStore serviceCodeStore, Clock clock) {
    super(Appointment.class, daoProvider);
    this.daoProvider = daoProvider;
    this.userProvider = userProvider;
    this.serviceCodeStore = serviceCodeStore;
    this.clock = clock;
  }

  @Override
  public QueryResult<Appointment> list(List query) {
    User user = userProvider.get();
    if (user.getRole() == Role.Nurse
        && query.getFilters().stream().map(Filter::getColumn).noneMatch(NURSE_ID::equals)) {
      query.addFilter(Filter.of(NURSE_ID, user.getId()));
    }
    query.getFilters().forEach(f -> {
      if (f.getColumn().equals("state") && f.getOperator().equals("=") && f.getValue().equals("PENDING_ASSESSMENT")) {
        query.setFilters(
            ImmutableList.<Filter>builder()
                .add(Filter.parse("state IN UNCONFIRMED|CONFIRMED|PENDING_ASSESSMENT" + (user.getRole() == Role.Nurse ? "" : "|PENDING_APPROVAL")))
                .addAll(query.getFilters().stream().filter(filter -> filter != f).collect(toImmutableList()))
                .add(Filter.parse("start<" + clock.millis()))
                // .add(Filter.parse("date<" + clock.millis()))
                .build());
        }
    });

    return super.list(query);
  }

  @Override
  protected Iterable<Op<Appointment>> preprocess(Iterable<Op<Appointment>> ops) {
    CachedReader cachedReader = new CachedReader(daoProvider.get());
    return stream(ops)
        .map(op -> updateNames(op, cachedReader))
        .map(this::updateStatus)
        .map(op -> updatePaymentInfo(op, cachedReader))
        .map(op -> updateBillingInfo(op, cachedReader))
        .collect(toImmutableList());
  }

  @Override
  protected Iterable<Appointment> transform(Iterable<Appointment> entities) {
    return stream(super.transform(entities))
        .map(this::updateStatus)
        .collect(toImmutableList());
  }

  private Op<Appointment> updateNames(Op<Appointment> op, CachedReader cachedReader) {
    Appointment appointment = op.getEntity();
    Patient patient = cachedReader.get(Patient.class, appointment.getPatientId());
    Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());

    if (appointment.getTitration() == null && patient != null) {
      appointment.setTitration(patient.getTitration());
    }

    appointment
        .setPatientName(patient == null ? null : patient.fullName())
        .setNurseName(nurse == null ? null : nurse.fullName())
        .setVendorId(patient == null ? null : patient.getBillingVendorId())
        .setVendorName(patient == null ? null : patient.getBillingVendorName());

    return op;
  }

  private Op<Appointment> updateStatus(Op<Appointment> op) {
    updateStatus(op.getEntity());
    return op;
  }

  private Appointment updateStatus(Appointment appointment) {
    if (appointment.getState() == AppointmentState.CLOSED) {
      return appointment;
    }

    if (appointment.getDeletionTime() != null) {
      return appointment.setState(AppointmentState.DELETED);
    }

    if (appointment.getTimeIn() != null && appointment.getTimeOut() != null) {
      long minsDiff =
          MILLISECONDS.toMinutes(appointment.getTimeOut().toEpochMilli() - appointment.getTimeIn().toEpochMilli());
      minsDiff = Math.round(minsDiff / 15.0) * 15;
      double hours = minsDiff / 60.0;
      if (hours < 0) {
        hours += 24;
      }
      appointment.setLoggedHours(hours);
    }

    if (appointment.isCancelled()) {
      appointment.setState(AppointmentState.CANCELLED);
    } else if (appointment.getExportId() != null) {
      appointment.setState(AppointmentState.CLOSED);
    } else if (appointment.isAssessmentApproved()) {
      appointment.setState(AppointmentState.BILLABLE_AND_PAYABLE);
    } else if (appointment.isAssessmentComplete()) {
      appointment.setState(AppointmentState.PENDING_APPROVAL);
    } else if (appointment.getDate().isBefore(Instant.ofEpochMilli(clock.millis()))) {
      appointment.setState(AppointmentState.PENDING_ASSESSMENT);
    } else if (appointment.getNurseConfirmTs() != null) {
      appointment.setState(AppointmentState.CONFIRMED);
    } else if (appointment.getNurseConfirmTs() == null) {
      appointment.setState(AppointmentState.UNCONFIRMED);
    }

    return appointment;
  }

  private Op<Appointment> updatePaymentInfo(Op<Appointment> op, CachedReader cachedReader) {
    Appointment appointment = op.getEntity();
    if (appointment.getPaymentInfo() == null && appointment.getLoggedHours() > 0) {
      appointment.setPaymentInfo(new AccountingInfo());
    }

    if (appointment.getPaymentInfo() == null) {
      return op;
    }

    Appointment original = op.getCurrent();
    if (original == null) {
      return op;
    }

    AccountingInfo paymentInfo = appointment.getPaymentInfo();
    var origPayment = original.getPaymentInfo() != null ? original.getPaymentInfo() : new AccountingInfo();

    if (appointment.getLoggedHours() != original.getLoggedHours() || appointment.getLoggedHours() > 0 && paymentInfo.getServiceCode() == null) {
      var desiredUnit = appointment.getLoggedHours() > 2 ? Hour : Visit;
      var serviceCode = serviceCodeStore
          .getForNurse(cachedReader.get(Nurse.class, appointment.getNurseId()), desiredUnit)
          .stream().filter(sc -> sc.getUnit() == desiredUnit).findFirst();
      if (serviceCode.isEmpty()) {
        serviceCode = serviceCodeStore
            .getForNurse(cachedReader.get(Nurse.class, appointment.getNurseId()), desiredUnit == Visit ? Hour : Visit)
            .stream().findFirst();
      }
      serviceCode.ifPresent(sc -> {
        paymentInfo.setServiceCode(sc.getId()).setUnitRate(sc.getUnitPrice()).setUnit(sc.getUnit());
        if (desiredUnit == sc.getUnit()) {
          paymentInfo.setUnitCount(desiredUnit == Visit ? 1 : appointment.getLoggedHours());
        } else {
          paymentInfo.setUnitCount(desiredUnit == Visit ? 2 : appointment.getLoggedHours() / 2);
        }
      });
    } else if (paymentInfo.getServiceCode() != null && (
        !Objects.equals(paymentInfo.getServiceCode(), origPayment.getServiceCode())
        || paymentInfo.getUnitCount() == 0
        || !Objects.equals(appointment.getNurseId(), original.getNurseId()))) {
      ServiceCode sc = serviceCodeStore.get(paymentInfo.getServiceCode());
      double unitCount = sc.getUnit() == Visit ? 1 : appointment.getLoggedHours();
      if (sc.getUnit() == Hour && unitCount > 0 && unitCount < 2) {
        unitCount = 2;
      }
      paymentInfo.setServiceCode(sc.getId()).setUnitRate(sc.getUnitPrice()).setUnit(sc.getUnit()).setUnitCount(unitCount);
    }

    if (appointment.getMileage() != original.getMileage()
        || appointment.getMileage() != 0 && (paymentInfo.getMileage() == null || paymentInfo.getMileageRate() == null)) {
      Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
      paymentInfo.setMileage(appointment.getMileage()).setMileageRate(nurse.getMileageRate());
    }

    appointment.setPaymentInfo(paymentInfo);
    return op;
  }

  private Op<Appointment> updateBillingInfo(Op<Appointment> op, CachedReader cachedReader) {
    Appointment appointment = op.getEntity();
    if (appointment.getBillingInfo() == null && appointment.getLoggedHours() > 0) {
      appointment.setBillingInfo(new AccountingInfo());
    }

    if (appointment.getBillingInfo() == null) {
      return op;
    }

    Appointment original = op.getCurrent();
    if (original == null) {
      return op;
    }

    AccountingInfo billingInfo = appointment.getBillingInfo();
    var origBilling = original.getBillingInfo() != null ? original.getBillingInfo() : new AccountingInfo();

    if (appointment.getLoggedHours() != original.getLoggedHours() || appointment.getLoggedHours() > 0 && billingInfo.getServiceCode() == null) {
      var serviceCodes = serviceCodeStore.getForVendor(appointment.getVendorId());
      var desiredUnit = appointment.getLoggedHours() > 2 ? Hour : Visit;
      var serviceCode = serviceCodes.stream().filter(sc -> sc.getUnit() == desiredUnit).findFirst();
      if (serviceCode.isEmpty()) {
        serviceCode = serviceCodes.stream().findFirst();
      }

      serviceCode.ifPresent(sc -> {
        billingInfo.setServiceCode(sc.getId()).setUnitRate(sc.getUnitPrice()).setUnit(sc.getUnit());
        if (desiredUnit == sc.getUnit()) {
          // If we were able to find the correct unit then we bill 1 visit or the number of hours.
          billingInfo.setUnitCount(desiredUnit == Visit ? 1 : appointment.getLoggedHours());
        } else {
          // If we were unable to find the correct unit, we bill 2 hours if it was supposed to be visit,
          // we bill 1 visit for every 2 hours spent if they only have visit pay.
          billingInfo.setUnitCount(desiredUnit == Visit ? 2 : appointment.getLoggedHours() / 2);
        }
      });
    } else if (billingInfo.getServiceCode() != null && (
        !Objects.equals(billingInfo.getServiceCode(), origBilling.getServiceCode())
        || billingInfo.getUnitCount() == 0
        || !Objects.equals(appointment.getVendorId(), original.getVendorId()))) {
      ServiceCode bc = serviceCodeStore.get(billingInfo.getServiceCode());
      double unitCount = bc.getUnit() == Visit ? 1 : appointment.getLoggedHours();
      if (bc.getUnit() == Hour && unitCount > 0 && unitCount < 2) {
        unitCount = 2;
      }
      billingInfo.setServiceCode(bc.getId()).setUnitRate(bc.getUnitPrice()).setUnit(bc.getUnit()).setUnitCount(unitCount);
    }

    if (appointment.getMileage() != original.getMileage()
        || appointment.getMileage() != 0 && (billingInfo.getMileage() == null || billingInfo.getMileageRate() == null)) {
      Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());
      billingInfo.setMileage(appointment.getMileage()).setMileageRate(vendor.getMileageRate());
    }

    appointment.setBillingInfo(billingInfo);
    return op;
  }
}
