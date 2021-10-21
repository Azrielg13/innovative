package com.digitald4.iis.model;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.digitald4.common.model.FileReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

public class Appointment {
  private long id;
  private long patientId;
  private String patientName;
  private long nurseId;
  private String nurseName;
  private long vendorId;
  private String vendorName;
  private long start;
  private long end;
  private boolean cancelled;
  private String cancelReason;
  private long nurseConfirmResId;
  private long nurseConfirmTs;
  private String nurseConfirmNotes;

  public enum AppointmentState {UNCONFIRMED, CONFIRMED, CANCELLED, PENDING_ASSESSMENT, PENDING_APPROVAL,
    BILLABLE, BILLABLE_AND_PAYABLE, PAYABLE, CLOSED};
  private AppointmentState state;

  private boolean assessmentComplete;
  private boolean assessmentApproved;
  private long approvedDate;
  private long approverId;

  private long timeIn;
  private long timeOut;
  private double loggedHours;
  private double mileage;

  public static class AccountingInfo {
    private long accountingTypeId;
    private double flatRate;
    private double hourlyRate;
    private double hours;
    private double subTotal;
    private double mileage;
    private double mileageRate;
    private double mileageTotal;
    private double total;

    public long getAccountingTypeId() {
      return accountingTypeId;
    }

    public AccountingInfo setAccountingTypeId(long accountingTypeId) {
      this.accountingTypeId = accountingTypeId;
      return this;
    }

    public double getFlatRate() {
      return flatRate;
    }

    public AccountingInfo setFlatRate(double flatRate) {
      this.flatRate = flatRate;
      return this;
    }

    public double getHourlyRate() {
      return hourlyRate;
    }

    public AccountingInfo setHourlyRate(double hourlyRate) {
      this.hourlyRate = hourlyRate;
      return this;
    }

    public double getHours() {
      return hours;
    }

    public AccountingInfo setHours(double hours) {
      this.hours = hours;
      return this;
    }

    public double getSubTotal() {
      return subTotal;
    }

    public AccountingInfo setSubTotal(double subTotal) {
      this.subTotal = subTotal;
      return this;
    }

    public double getMileage() {
      return mileage;
    }

    public AccountingInfo setMileage(double mileage) {
      this.mileage = mileage;
      return this;
    }

    public double getMileageRate() {
      return mileageRate;
    }

    public AccountingInfo setMileageRate(double mileageRate) {
      this.mileageRate = mileageRate;
      return this;
    }

    public double getMileageTotal() {
      return mileageTotal;
    }

    public AccountingInfo setMileageTotal(double mileageTotal) {
      this.mileageTotal = mileageTotal;
      return this;
    }

    public double getTotal() {
      return total;
    }

    public AccountingInfo setTotal(double total) {
      this.total = total;
      return this;
    }
  }
  private AccountingInfo paymentInfo;
  private long paystubId;
  private AccountingInfo billingInfo;
  private long invoiceId;

  public static class Assessment {
    private long typeId;
    private String value;

    public Assessment() {}

    public Assessment(long typeId, String value) {
      this.typeId = typeId;
      this.value = value;
    }

    public long getTypeId() {
      return typeId;
    }

    public Assessment setTypeId(long typeId) {
      this.typeId = typeId;
      return this;
    }

    public String getValue() {
      return value;
    }

    public Assessment setValue(String value) {
      this.value = value;
      return this;
    }
  }
  private ImmutableMap<Long, Assessment> assessments;

  private FileReference assessmentReport;

  public long getId() {
    return id;
  }

  public Appointment setId(long id) {
    this.id = id;
    return this;
  }

  public long getPatientId() {
    return patientId;
  }

  public Appointment setPatientId(long patientId) {
    this.patientId = patientId;
    return this;
  }

  public String getPatientName() {
    return patientName;
  }

  public Appointment setPatientName(String patientName) {
    this.patientName = patientName;
    return this;
  }

  public long getNurseId() {
    return nurseId;
  }

  public Appointment setNurseId(long nurseId) {
    this.nurseId = nurseId;
    return this;
  }

  public String getNurseName() {
    return nurseName;
  }

  public Appointment setNurseName(String nurseName) {
    this.nurseName = nurseName;
    return this;
  }

  public long getVendorId() {
    return vendorId;
  }

  public Appointment setVendorId(long vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  public String getVendorName() {
    return vendorName;
  }

  public Appointment setVendorName(String vendorName) {
    this.vendorName = vendorName;
    return this;
  }

  public long getStart() {
    return start;
  }

  public Appointment setStart(long start) {
    this.start = start;
    return this;
  }

  public long getEnd() {
    return end;
  }

  public Appointment setEnd(long end) {
    this.end = end;
    return this;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public Appointment setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
    return this;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public Appointment setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
    return this;
  }

  public long getNurseConfirmResId() {
    return nurseConfirmResId;
  }

  public Appointment setNurseConfirmResId(long nurseConfirmResId) {
    this.nurseConfirmResId = nurseConfirmResId;
    return this;
  }

  public long getNurseConfirmTs() {
    return nurseConfirmTs;
  }

  public Appointment setNurseConfirmTs(long nurseConfirmTs) {
    this.nurseConfirmTs = nurseConfirmTs;
    return this;
  }

  public String getNurseConfirmNotes() {
    return nurseConfirmNotes;
  }

  public Appointment setNurseConfirmNotes(String nurseConfirmNotes) {
    this.nurseConfirmNotes = nurseConfirmNotes;
    return this;
  }

  public AppointmentState getState() {
    return state;
  }

  public Appointment setState(AppointmentState state) {
    this.state = state;
    return this;
  }

  public boolean isAssessmentComplete() {
    return assessmentComplete;
  }

  public Appointment setAssessmentComplete(boolean assessmentComplete) {
    this.assessmentComplete = assessmentComplete;
    return this;
  }

  public boolean isAssessmentApproved() {
    return assessmentApproved;
  }

  public Appointment setAssessmentApproved(boolean assessmentApproved) {
    this.assessmentApproved = assessmentApproved;
    return this;
  }

  public long getApprovedDate() {
    return approvedDate;
  }

  public Appointment setApprovedDate(long approvedDate) {
    this.approvedDate = approvedDate;
    return this;
  }

  public long getApproverId() {
    return approverId;
  }

  public Appointment setApproverId(long approverId) {
    this.approverId = approverId;
    return this;
  }

  public long getTimeIn() {
    return timeIn;
  }

  public Appointment setTimeIn(long timeIn) {
    this.timeIn = timeIn;
    return this;
  }

  public long getTimeOut() {
    return timeOut;
  }

  public Appointment setTimeOut(long timeOut) {
    this.timeOut = timeOut;
    return this;
  }

  public double getLoggedHours() {
    return loggedHours;
  }

  public Appointment setLoggedHours(double loggedHours) {
    this.loggedHours = loggedHours;
    return this;
  }

  public double getMileage() {
    return mileage;
  }

  public Appointment setMileage(double mileage) {
    this.mileage = mileage;
    return this;
  }

  public AccountingInfo getPaymentInfo() {
    return paymentInfo;
  }

  public Appointment setPaymentInfo(AccountingInfo paymentInfo) {
    this.paymentInfo = paymentInfo;
    return this;
  }

  public long getPaystubId() {
    return paystubId;
  }

  public Appointment setPaystubId(long paystubId) {
    this.paystubId = paystubId;
    return this;
  }

  public AccountingInfo getBillingInfo() {
    return billingInfo;
  }

  public Appointment setBillingInfo(AccountingInfo billingInfo) {
    this.billingInfo = billingInfo;
    return this;
  }

  public long getInvoiceId() {
    return invoiceId;
  }

  public Appointment setInvoiceId(long invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }

  public ImmutableList<Assessment> getAssessments() {
    return assessments.values().asList();
  }

  public Appointment setAssessments(Iterable<Assessment> assessments) {
    this.assessments = Streams.stream(assessments).collect(toImmutableMap(Assessment::getTypeId, identity()));
    return this;
  }

  public Assessment getAssessment(long typeId) {
    return assessments.get(typeId);
  }

  public FileReference getAssessmentReport() {
    return assessmentReport;
  }

  public Appointment setAssessmentReport(FileReference assessmentReport) {
    this.assessmentReport = assessmentReport;
    return this;
  }
}
