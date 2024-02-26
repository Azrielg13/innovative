package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.CachedReader;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.List;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AppointmentState;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;

import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;

public class AppointmentStore extends GenericLongStore<Appointment> {
  private final Provider<DAO> daoProvider;
  private final Clock clock;

  @Inject
  public AppointmentStore(Provider<DAO> daoProvider, Clock clock) {
    super(Appointment.class, daoProvider);
    this.daoProvider = daoProvider;
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
              .add(Filter.parse("end<" + clock.millis()))
              .build());
    }

    return super.list(query);
  }

  @Override
  protected Iterable<Appointment> preprocess(Iterable<Appointment> entities, boolean isCreate) {
    CachedReader cachedReader = new CachedReader(daoProvider.get());
    return stream(super.preprocess(entities, isCreate))
        .map(appointment -> updateNames(appointment, cachedReader))
        .peek(this::updateStatus)
        .map(appointment -> updatePaymentInfo(appointment, cachedReader))
        .map(appointment -> updateBillingInfo(appointment, cachedReader))
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
        .setPatientName(patient == null ? null : patient.getName())
        .setNurseName(nurse == null ? null : nurse.fullName())
        .setVendorName(vendor == null ? null : vendor.getName());
  }

  private Appointment updatePaymentInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getPaymentInfo() == null) {
      return appointment;
    }
    Appointment original = cachedReader.get(Appointment.class, appointment.getId());

    AccountingInfo paymentInfo = appointment.getPaymentInfo();
    AccountingInfo origPayment = original.getPaymentInfo() != null ? original.getPaymentInfo() : new AccountingInfo();
    // If the payment type has been changed we need to fill in payment amounts.
    if (!Objects.equals(paymentInfo.getAccountingTypeId(), origPayment.getAccountingTypeId())
        || appointment.getLoggedHours() != original.getLoggedHours()
        || !Objects.equals(appointment.getNurseId(), original.getNurseId())) {
      Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
      if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_FIXED) {
        paymentInfo
            .setFlatRate(nurse.getPayFlat())
            .setHourlyRate(0)
            .setHours(appointment.getLoggedHours());
      } else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_HOURLY) {
        paymentInfo
            .setFlatRate(0)
            .setHourlyRate(nurse.getPayRate())
            .setHours(appointment.getLoggedHours());
      } else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_ROC2_HR) {
        paymentInfo
            .setFlatRate(nurse.getPayFlat2HrRoc())
            .setHourlyRate(nurse.getPayRate2HrRoc())
            .setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
      } else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_SOC2_HR) {
        paymentInfo
            .setFlatRate(nurse.getPayFlat2HrSoc())
            .setHourlyRate(nurse.getPayRate2HrSoc())
            .setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
      }
    }
    if (paymentInfo.getMileage() != 0 || appointment.getMileage() != original.getMileage()) {
      paymentInfo.setMileage(appointment.getMileage());
    }
    if (paymentInfo.getMileage() != origPayment.getMileage()) {
      Nurse nurse = cachedReader.get(Nurse.class, appointment.getNurseId());
      paymentInfo.setMileageRate(nurse.getMileageRate());
    }

    return appointment.setPaymentInfo(
        paymentInfo
            .setSubTotal(paymentInfo.getFlatRate() + paymentInfo.getHours() * paymentInfo.getHourlyRate())
            .setMileageTotal(paymentInfo.getMileage() * paymentInfo.getMileageRate())
            .setTotal(paymentInfo.getSubTotal() + paymentInfo.getMileageTotal()));
  }

  private Appointment updateBillingInfo(Appointment appointment, CachedReader cachedReader) {
    if (appointment.getBillingInfo() == null) {
      return appointment;
    }

    Appointment original = cachedReader.get(Appointment.class, appointment.getId());
    AccountingInfo billingInfo = appointment.getBillingInfo();
    AccountingInfo origBilling = original.getBillingInfo() != null ? original.getBillingInfo() : new AccountingInfo();

    // If the payment type has been changed we need to fill in payment amounts.
    if (!Objects.equals(billingInfo.getAccountingTypeId(), origBilling.getAccountingTypeId())
        || appointment.getLoggedHours() != original.getLoggedHours()
        || !Objects.equals(appointment.getVendorId(), original.getVendorId())) {
      Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());
      if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_FIXED) {
        billingInfo
            .setFlatRate(vendor.getBillingFlat())
            .setHourlyRate(0)
            .setHours(appointment.getLoggedHours());
      } else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_HOURLY) {
        billingInfo
            .setFlatRate(0)
            .setHourlyRate(vendor.getBillingRate())
            .setHours(appointment.getLoggedHours());
      } else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_ROC2_HR) {
        billingInfo
            .setFlatRate(vendor.getBillingFlat2HrRoc())
            .setHourlyRate(vendor.getBillingRate2HrRoc())
            .setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
      } else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_SOC2_HR) {
        billingInfo
            .setFlatRate(vendor.getBillingFlat2HrSoc())
            .setHourlyRate(vendor.getBillingRate2HrSoc())
            .setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
      }
    }
    if (billingInfo.getMileage() != 0 || appointment.getMileage() != original.getMileage()) {
      billingInfo.setMileage(appointment.getMileage());
    }
    if (billingInfo.getMileage() != origBilling.getMileage()) {
      Vendor vendor = cachedReader.get(Vendor.class, appointment.getVendorId());
      billingInfo.setMileageRate(vendor.getMileageRate());
    }

    return appointment.setBillingInfo(
        billingInfo
            .setSubTotal(billingInfo.getFlatRate() + billingInfo.getHours() * billingInfo.getHourlyRate())
            .setMileageTotal(billingInfo.getMileage() * billingInfo.getMileageRate())
            .setTotal(billingInfo.getSubTotal() + billingInfo.getMileageTotal()));
  }

  private Appointment updateStatus(Appointment appointment) {
    if (appointment.getTimeIn() != null && appointment.getTimeOut() != null) {
      long minsDiff = TimeUnit.MILLISECONDS.toMinutes(
          appointment.getTimeOut().toEpochMilli() - appointment.getTimeIn().toEpochMilli());
      minsDiff = Math.round(minsDiff / 15.0) * 15;
      double hours = minsDiff / 60.0;
      if (hours < 0) {
        hours += 24;
      }
      appointment.setLoggedHours(hours);
    }

    if (appointment.isCancelled()) {
      appointment.setState(AppointmentState.CANCELLED);
    } else if (appointment.getInvoiceId() != null && appointment.getPaystubId() != null) {
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
    } else if (appointment.getStart().isBefore(Instant.ofEpochMilli(clock.millis()))) {
      appointment.setState(AppointmentState.PENDING_ASSESSMENT);
    } else if (appointment.getNurseConfirmTs() != null) {
      appointment.setState(AppointmentState.CONFIRMED);
    }

    return appointment;
  }
}
