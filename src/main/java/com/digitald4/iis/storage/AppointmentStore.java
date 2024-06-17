package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.ServiceCode.Unit;
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
    if (query.getFilters().stream().anyMatch(f ->
        f.getColumn().equals("state") && f.getOperator().equals("=") && f.getValue().equals("PENDING_ASSESSMENT"))) {
      query.setFilters(
          ImmutableList.<Filter>builder()
              .add(Filter.parse("state IN UNCONFIRMED|CONFIRMED|PENDING_ASSESSMENT"))
              .addAll(query.getFilters().subList(1, query.getFilters().size()))
              .add(Filter.parse("date<" + clock.millis()))
              // .add(Filter.parse("date<" + clock.millis()))
              .build());
    }

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

  private Appointment updateNames(Appointment appointment, CachedReader cachedReader) {
    Patient patient = cachedReader.get(Patient.class, appointment.getPatientId());
    Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
    if (appointment.getVendorId() == null && patient != null && patient.getBillingVendorId() != null) {
      appointment.setVendorId(patient.getBillingVendorId());
    }
    Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());

    return appointment
        .setPatientName(patient == null ? null : patient.fullName())
        .setNurseName(nurse == null ? null : nurse.fullName())
        .setVendorName(vendor == null ? null : vendor.getName());
  }

  private Appointment updatePaymentInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getPaymentInfo() == null) {
      return appointment;
    }

    Appointment original = cachedReader.get(Appointment.class, appointment.getId());
    if (original == null) {
      return appointment;
    }

    AccountingInfo paymentInfo = appointment.getPaymentInfo();
    AccountingInfo origPayment = original.getPaymentInfo() != null ? original.getPaymentInfo() : new AccountingInfo();

    if (paymentInfo.getServiceCode() != null && (
        !Objects.equals(paymentInfo.getServiceCode(), origPayment.getServiceCode())
        || appointment.getLoggedHours() != original.getLoggedHours()
        || !Objects.equals(appointment.getNurseId(), original.getNurseId()))) {
      ServiceCode serviceCode = serviceCodeStore.get(paymentInfo.getServiceCode());
      paymentInfo.setUnitRate(serviceCode.getUnitPrice()).setUnit(serviceCode.getUnit());
    }

    if (appointment.getMileage() != 0 && paymentInfo.getMileageRate() == 0
        || appointment.getMileage() != original.getMileage()) {
      Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
      paymentInfo.setMileageRate(nurse.getMileageRate());
    }

    Unit unit = paymentInfo.getUnit();
    return appointment.setPaymentInfo(
        paymentInfo
            .setSubTotal(paymentInfo.getUnitRate() * (unit == Unit.Visit ? 1 : appointment.getLoggedHours()))
            .setMileageTotal(appointment.getMileage() * paymentInfo.getMileageRate())
            .setTotal(paymentInfo.getSubTotal() + paymentInfo.getMileageTotal()));
  }

  private Appointment updateBillingInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getBillingInfo() == null) {
      return appointment;
    }

    Appointment original = cachedReader.get(Appointment.class, appointment.getId());
    if (original == null) {
      return appointment;
    }

    AccountingInfo billingInfo = appointment.getBillingInfo();
    AccountingInfo origBilling = original.getBillingInfo() != null ? original.getBillingInfo() : new AccountingInfo();

    if (billingInfo.getServiceCode() != null && (
        !Objects.equals(billingInfo.getServiceCode(), origBilling.getServiceCode())
        || appointment.getLoggedHours() != original.getLoggedHours()
        || !Objects.equals(appointment.getVendorId(), original.getVendorId()))) {
      ServiceCode billCode = serviceCodeStore.get(billingInfo.getServiceCode());
      billingInfo.setUnitRate(billCode.getUnitPrice()).setUnit(billCode.getUnit());
    }

    if (appointment.getMileage() != 0 && billingInfo.getMileageRate() == 0
        || appointment.getMileage() != original.getMileage()) {
      Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());
      billingInfo.setMileageRate(vendor.getMileageRate());
    }

    Unit unit = billingInfo.getUnit();
    return appointment.setBillingInfo(
        billingInfo
            .setSubTotal(billingInfo.getUnitRate() * (unit == Unit.Visit ? 1 : appointment.getLoggedHours()))
            .setMileageTotal(appointment.getMileage() * billingInfo.getMileageRate())
            .setTotal(billingInfo.getSubTotal() + billingInfo.getMileageTotal()));
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
    } else if (appointment.getExportId() != null || appointment.getInvoiceId() != null && appointment.getPaystubId() != null) {
      appointment.setState(AppointmentState.CLOSED);
    } else if (appointment.isAssessmentApproved()) {
      if (appointment.getInvoiceId() == null && appointment.getPaystubId() == null) {
        appointment.setState(AppointmentState.BILLABLE_AND_PAYABLE);
      } else if (appointment.getPaystubId() == null) {
        appointment.setState(AppointmentState.PAYABLE);
      } else {
        appointment.setState(AppointmentState.BILLABLE);
      }
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
