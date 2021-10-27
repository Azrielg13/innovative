package com.digitald4.iis.model;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.digitald4.common.model.FileReference;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import org.joda.time.DateTime;

public class Appointment {
  private long id;
  private long patientId;
  private String patientName;
  private long nurseId;
  private String nurseName;
  private long vendorId;
  private String vendorName;
  private DateTime start;
  private DateTime end;
  private boolean cancelled;
  private String cancelReason;
  private Long nurseConfirmResId;
  private DateTime nurseConfirmTs;
  private String nurseConfirmNotes;
  public enum AppointmentState {UNCONFIRMED, CONFIRMED, CANCELLED, PENDING_ASSESSMENT, PENDING_APPROVAL,
    BILLABLE, BILLABLE_AND_PAYABLE, PAYABLE, CLOSED};
  private AppointmentState state;

  private boolean assessmentComplete;
  private boolean assessmentApproved;
  private DateTime approvalTs;
  private Long approverId;

  private DateTime timeIn;
  private DateTime timeOut;
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
  private Long paystubId;
  private AccountingInfo billingInfo;
  private Long invoiceId;

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
  private ImmutableMap<Long, Assessment> assessments = ImmutableMap.of();

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

  @ApiResourceProperty
  public long start() {
    return start == null ? 0 : start.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getStart() {
    return start;
  }

  public Appointment setStart(DateTime start) {
    this.start = start;
    return this;
  }

  @ApiResourceProperty
  public long end() {
    return end == null ? 0 : end.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getEnd() {
    return end;
  }

  public Appointment setEnd(DateTime end) {
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

  public Long getNurseConfirmResId() {
    return nurseConfirmResId;
  }

  public Appointment setNurseConfirmResId(Long nurseConfirmResId) {
    this.nurseConfirmResId = nurseConfirmResId;
    return this;
  }

  @ApiResourceProperty
  public Long nurseConfirmTs() {
    return nurseConfirmTs == null ? null : nurseConfirmTs.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getNurseConfirmTs() {
    return nurseConfirmTs;
  }

  public Appointment setNurseConfirmTs(DateTime nurseConfirmTs) {
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

  @ApiResourceProperty
  public Long approvalTs() {
    return approvalTs == null ? null : approvalTs.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getApprovalTs() {
    return approvalTs;
  }

  public Appointment setApprovedDate(DateTime approvalTs) {
    this.approvalTs = approvalTs;
    return this;
  }

  public Long getApproverId() {
    return approverId;
  }

  public Appointment setApproverId(Long approverId) {
    this.approverId = approverId;
    return this;
  }

  @ApiResourceProperty
  public Long timeIn() {
    return timeIn == null ? null : timeIn.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getTimeIn() {
    return timeIn;
  }

  public Appointment setTimeIn(DateTime timeIn) {
    this.timeIn = timeIn;
    return this;
  }

  @ApiResourceProperty
  public Long timeOut() {
    return timeOut == null ? null : timeOut.getMillis();
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public DateTime getTimeOut() {
    return timeOut;
  }

  public Appointment setTimeOut(DateTime timeOut) {
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

  public Long getPaystubId() {
    return paystubId;
  }

  public Appointment setPaystubId(Long paystubId) {
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

  public Long getInvoiceId() {
    return invoiceId;
  }

  public Appointment setInvoiceId(Long invoiceId) {
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
