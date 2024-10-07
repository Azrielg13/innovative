package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;
import static com.digitald4.iis.model.ServiceCode.Unit.Hour;
import static com.digitald4.iis.model.ServiceCode.Unit.Visit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AppointmentState;
import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class AppointmentStore extends GenericStore<Appointment, Long> {
  private final Provider<DAO> daoProvider;
  private final ServiceCodeStore serviceCodeStore;
  private final Clock clock;

  @Inject
  public AppointmentStore(Provider<DAO> daoProvider, ServiceCodeStore serviceCodeStore, Clock clock) {
    super(Appointment.class, daoProvider);
    this.daoProvider = daoProvider;
    this.serviceCodeStore = serviceCodeStore;
    this.clock = clock;
  }

  @Override
  public QueryResult<Appointment> list(List query) {
    query.getFilters().forEach(f -> {
      if (f.getColumn().equals("state") && f.getOperator().equals("=") && f.getValue().equals("PENDING_ASSESSMENT")) {
        query.setFilters(
            ImmutableList.<Filter>builder()
                .add(Filter.parse("state IN UNCONFIRMED|CONFIRMED|PENDING_ASSESSMENT"))
                .addAll(query.getFilters().stream().filter(filter -> filter != f).collect(toImmutableList()))
                .add(Filter.parse("start<" + clock.millis()))
                // .add(Filter.parse("date<" + clock.millis()))
                .build());
        }
    });

    return super.list(query);
  }

  @Override
  protected Iterable<Appointment> preprocess(Iterable<Appointment> entities, boolean isCreate) {
    CachedReader cachedReader = new CachedReader(daoProvider.get());
    return stream(super.preprocess(entities, isCreate))
        .map(appointment -> updateNames(appointment, cachedReader))
        .map(this::updateStatus)
        .map(appointment -> isCreate ? appointment : updatePaymentInfo(appointment, cachedReader))
        .map(appointment -> isCreate ? appointment : updateBillingInfo(appointment, cachedReader))
        .collect(toImmutableList());
  }

  @Override
  protected Iterable<Appointment> transform(Iterable<Appointment> entities) {
    return stream(super.transform(entities))
        .map(this::updateStatus)
        .collect(toImmutableList());
  }

  private Appointment updateNames(Appointment appointment, CachedReader cachedReader) {
    Patient patient = cachedReader.get(Patient.class, appointment.getPatientId());
    Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());

    if (appointment.getTitration() == null && patient != null) {
      appointment.setTitration(patient.getTitration());
    }

    return appointment
        .setPatientName(patient == null ? null : patient.fullName())
        .setNurseName(nurse == null ? null : nurse.fullName())
        .setVendorId(patient == null ? null : patient.getBillingVendorId())
        .setVendorName(patient == null ? null : patient.getBillingVendorName());
  }

  private Appointment updatePaymentInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getPaymentInfo() == null && appointment.getLoggedHours() > 0) {
      appointment.setPaymentInfo(new AccountingInfo());
    }

    if (appointment.getPaymentInfo() == null) {
      return appointment;
    }

    Appointment original = cachedReader.get(Appointment.class, appointment.getId());
    if (original == null) {
      return appointment;
    }

    AccountingInfo paymentInfo = appointment.getPaymentInfo();
    AccountingInfo origPayment = original.getPaymentInfo() != null ? original.getPaymentInfo() : new AccountingInfo();

    if (appointment.getLoggedHours() != original.getLoggedHours()) {
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
      ServiceCode serviceCode = serviceCodeStore.get(paymentInfo.getServiceCode());
      double unitCount = serviceCode.getUnit() == Visit ? 1 : appointment.getLoggedHours();
      if (serviceCode.getUnit() == Hour && unitCount > 0 && unitCount < 2) {
        unitCount = 2;
      }
      paymentInfo.setUnitRate(serviceCode.getUnitPrice()).setUnit(serviceCode.getUnit()).setUnitCount(unitCount);
    }

    if (appointment.getMileage() != original.getMileage()
        || appointment.getMileage() != 0 && (paymentInfo.getMileage() == null || paymentInfo.getMileageRate() == null)) {
      Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
      paymentInfo.setMileage(appointment.getMileage()).setMileageRate(nurse.getMileageRate());
    }

    return appointment.setPaymentInfo(paymentInfo);
  }

  private Appointment updateBillingInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getBillingInfo() == null && appointment.getLoggedHours() > 0) {
      appointment.setBillingInfo(new AccountingInfo());
    }

    if (appointment.getBillingInfo() == null) {
      return appointment;
    }

    Appointment original = cachedReader.get(Appointment.class, appointment.getId());
    if (original == null) {
      return appointment;
    }

    AccountingInfo billingInfo = appointment.getBillingInfo();
    AccountingInfo origBilling = original.getBillingInfo() != null ? original.getBillingInfo() : new AccountingInfo();

    if (appointment.getLoggedHours() != original.getLoggedHours()) {
      var serviceCodes = serviceCodeStore.getForVendor(appointment.getVendorId());
      var desiredUnit = appointment.getLoggedHours() > 2 ? Hour : Visit;
      var serviceCode = serviceCodes.stream().filter(sc -> sc.getUnit() == desiredUnit).findFirst();
      if (serviceCode.isEmpty()) {
        serviceCode = serviceCodes.stream().findFirst();
      }

      serviceCode.ifPresent(sc -> {
        billingInfo.setServiceCode(sc.getId()).setUnitRate(sc.getUnitPrice()).setUnit(sc.getUnit());
        if (desiredUnit == sc.getUnit()) {
          billingInfo.setUnitCount(desiredUnit == Visit ? 1 : appointment.getLoggedHours());
        } else {
          billingInfo.setUnitCount(desiredUnit == Visit ? 2 : appointment.getLoggedHours() / 2);
        }
      });
    } else if (billingInfo.getServiceCode() != null && (
        !Objects.equals(billingInfo.getServiceCode(), origBilling.getServiceCode())
        || billingInfo.getUnitCount() == 0
        || !Objects.equals(appointment.getVendorId(), original.getVendorId()))) {
      ServiceCode billCode = serviceCodeStore.get(billingInfo.getServiceCode());
      double unitCount = billCode.getUnit() == Visit ? 1 : appointment.getLoggedHours();
      if (billCode.getUnit() == Hour && unitCount > 0 && unitCount < 2) {
        unitCount = 2;
      }
      billingInfo.setUnitRate(billCode.getUnitPrice()).setUnit(billCode.getUnit()).setUnitCount(unitCount);
    }

    if (appointment.getMileage() != original.getMileage()
        || appointment.getMileage() != 0 && (billingInfo.getMileage() == null || billingInfo.getMileageRate() == null)) {
      Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());
      billingInfo.setMileage(appointment.getMileage()).setMileageRate(vendor.getMileageRate());
    }

    return appointment.setBillingInfo(billingInfo);
  }

  private Appointment updateStatus(Appointment appointment) {
    if (appointment.getState() == AppointmentState.CLOSED) {
      return appointment;
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
    }

    return appointment;
  }
}
