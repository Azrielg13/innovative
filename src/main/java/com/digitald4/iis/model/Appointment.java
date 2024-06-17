package com.digitald4.iis.model;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.Streams.stream;
import static java.util.function.Function.identity;

import com.digitald4.common.model.FileReference;
import com.digitald4.common.model.ModelObjectModUser;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.storage.GenData;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.*;

public class Appointment extends ModelObjectModUser<Long> {
  private static String TIME_ZONE = "America/Los_Angeles";
  public static final int ASSESSMENT_TOTAL = 74;

  private Long patientId;
  private String patientName;
  private Long nurseId;
  private String nurseName;
  private Long vendorId;
  private String vendorName;
  private Instant date;
  private Instant startTime;
  private Instant endTime;
  private boolean cancelled;
  private String cancelReason;
  private Long nurseConfirmResId;
  private Instant nurseConfirmTs;
  private String nurseConfirmNotes;
  public enum AppointmentState {UNCONFIRMED, CONFIRMED, CANCELLED, PENDING_ASSESSMENT,
    PENDING_APPROVAL, BILLABLE_AND_PAYABLE, BILLABLE, PAYABLE, @Deprecated EXPORTED, CLOSED};
  private AppointmentState state = AppointmentState.UNCONFIRMED;

  private boolean assessmentComplete;
  private boolean assessmentApproved;
  private Instant approvalTs;
  private Long approverId;

  private Instant timeIn;
  private Instant timeOut;
  private double loggedHours;
  private double mileage;
  private String fromZipCode;
  private String toZipCode;
  private AccountingInfo paymentInfo;
  private Long paystubId;
  private AccountingInfo billingInfo;
  private Long invoiceId;
  private String exportId;
  private ImmutableMap<Long, Assessment> assessments = ImmutableMap.of();
  private FileReference assessmentReport;

  public Appointment setId(Long id) {
    super.setId(id);
    return this;
  }

  public Long getPatientId() {
    return patientId;
  }

  public Appointment setPatientId(Long patientId) {
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

  public Long getNurseId() {
    return nurseId;
  }

  public Appointment setNurseId(Long nurseId) {
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

  public Long getVendorId() {
    return vendorId;
  }

  public Appointment setVendorId(Long vendorId) {
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

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  @Deprecated public Instant getStart() {
    return date;
  }

  @Deprecated public Appointment setStart(Instant start) {
    DateTime dateTime = new DateTime(start.toEpochMilli());
    long startTime = dateTime.getMillisOfDay();
    if (date == null) {
      date = Instant.ofEpochMilli(dateTime.getMillis() - startTime);
    }
    if (this.startTime == null) {
      this.startTime = Instant.ofEpochMilli(startTime);
    }
    return this;
  }

  @ApiResourceProperty
  public long start() {
    return (date == null ? 0 : date.toEpochMilli()) + (startTime == null ? 0 : startTime.toEpochMilli());
  }

  @Deprecated public Appointment setStart(long start) {
    return setStart(Instant.ofEpochMilli(start));
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  @Deprecated public Instant getEnd() {
    return null;
  }

  @Deprecated public Appointment setEnd(Instant end) {
    return this;
  }

  @ApiResourceProperty
  public long end() {
    return (date == null ? 0 : date.toEpochMilli()) + (endTime == null ? 0 : endTime.toEpochMilli());
  }

  @Deprecated public Appointment setEnd(long end) {
    return setEnd(Instant.ofEpochMilli(end));
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDate() {
    return date;
  }

  public Appointment setDate(Instant date) {
    this.date = date;
    return this;
  }

  @ApiResourceProperty
  public long date() {
    return date == null ? 0 : date.toEpochMilli();
  }

  public Appointment setDate(long date) {
    this.date = Instant.ofEpochMilli(date);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getStartTime() {
    return startTime;
  }

  public Appointment setStartTime(Instant startTime) {
    this.startTime = startTime;
    return this;
  }

  @ApiResourceProperty
  public long startTime() {
    return startTime == null ? 0 : startTime.toEpochMilli();
  }

  public Appointment setStartTime(long startTime) {
    this.startTime = Instant.ofEpochMilli(startTime);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getEndTime() {
    return endTime;
  }

  public Appointment setEndTime(Instant endTime) {
    this.endTime = endTime;
    return this;
  }

  @ApiResourceProperty
  public long endTime() {
    return endTime == null ? 0 : endTime.toEpochMilli();
  }

  public Appointment setEndTime(long endTime) {
    this.endTime = Instant.ofEpochMilli(endTime);
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

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getNurseConfirmTs() {
    return nurseConfirmTs;
  }

  public Appointment setNurseConfirmTs(Instant nurseConfirmTs) {
    this.nurseConfirmTs = nurseConfirmTs;
    return this;
  }

  @ApiResourceProperty
  public Long nurseConfirmTs() {
    return nurseConfirmTs == null ? null : nurseConfirmTs.toEpochMilli();
  }

  public Appointment setNurseConfirmTs(long nurseConfirmTs) {
    this.nurseConfirmTs = Instant.ofEpochMilli(nurseConfirmTs);
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

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getApprovalTs() {
    return approvalTs;
  }

  public Appointment setApprovedDate(Instant approvalTs) {
    this.approvalTs = approvalTs;
    return this;
  }

  @ApiResourceProperty
  public Long approvalTs() {
    return approvalTs == null ? null : approvalTs.toEpochMilli();
  }

  public Appointment setApprovedDate(long approvalTs) {
    this.approvalTs = Instant.ofEpochMilli(approvalTs);
    return this;
  }

  public Long getApproverId() {
    return approverId;
  }

  public Appointment setApproverId(Long approverId) {
    this.approverId = approverId;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getTimeIn() {
    return timeIn;
  }

  public Appointment setTimeIn(Instant timeIn) {
    this.timeIn = timeIn;
    return this;
  }

  @ApiResourceProperty
  public Long timeIn() {
    return timeIn == null ? null : timeIn.toEpochMilli();
  }

  public Appointment setTimeIn(long timeIn) {
    this.timeIn = Instant.ofEpochMilli(timeIn);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getTimeOut() {
    return timeOut;
  }

  public Appointment setTimeOut(Instant timeOut) {
    this.timeOut = timeOut;
    return this;
  }

  @ApiResourceProperty
  public Long timeOut() {
    return timeOut == null ? null : timeOut.toEpochMilli();
  }

  public Appointment setTimeOut(long timeOut) {
    this.timeOut = Instant.ofEpochMilli(timeOut);
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

  public String getFromZipCode() {
    return fromZipCode;
  }

  public Appointment setFromZipCode(String fromZipCode) {
    this.fromZipCode = fromZipCode;
    return this;
  }

  public String getToZipCode() {
    return toZipCode;
  }

  public Appointment setToZipCode(String toZipCode) {
    this.toZipCode = toZipCode;
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

  public String getExportId() {
    return exportId;
  }

  public Appointment setExportId(String exportId) {
    this.exportId = exportId;
    return this;
  }

  public ImmutableList<Assessment> getAssessments() {
    return assessments.values().asList();
  }

  public Appointment setAssessments(Iterable<Assessment> assessments) {
    this.assessments =
        stream(assessments).collect(toImmutableMap(Assessment::getTypeId, identity()));
    return this;
  }

  public Assessment getAssessment(long typeId) {
    return assessments.get(typeId);
  }

  @ApiResourceProperty
  public double assPercentComplete() {
    return Calculate.round(getAssessments().size() * 100.0 / ASSESSMENT_TOTAL, 1);
  }

  public FileReference getAssessmentReport() {
    return assessmentReport;
  }

  public Appointment setAssessmentReport(FileReference assessmentReport) {
    this.assessmentReport = assessmentReport;
    return this;
  }

  public static class AccountingInfo {
    public enum AccountingType {Auto_Detect, Hourly, Fixed, Soc2Hr, Roc2Hr};
    private AccountingType accountingType;
    private String serviceCode;
    private ServiceCode.Unit unit;
    private double unitRate;
    private double flatRate;
    private double hourlyRate;
    private double hours;
    private double subTotal;
    private double mileageRate;
    private double mileageTotal;
    private double total;

    public AccountingType getAccountingType() {
      return accountingType;
    }

    public AccountingInfo setAccountingType(AccountingType accountingType) {
      this.accountingType = accountingType;
      return this;
    }

    @Deprecated
    public Long getAccountingTypeId() {
      return null;
    }

    @Deprecated
    public AccountingInfo setAccountingTypeId(int accountingTypeId) {
      this.accountingType = switch (accountingTypeId) {
        case GenData.ACCOUNTING_TYPE_FIXED -> AccountingType.Fixed;
        case GenData.ACCOUNTING_TYPE_HOURLY -> AccountingType.Hourly;
        case GenData.ACCOUNTING_TYPE_SOC2_HR -> AccountingType.Soc2Hr;
        case GenData.ACCOUNTING_TYPE_ROC2_HR -> AccountingType.Roc2Hr;
        default -> AccountingType.Auto_Detect;
      };
      return this;
    }

    @Deprecated
    public String getBillCode() {
      return null;
    }

    @Deprecated
    public AccountingInfo setBillCode(String billCode) {
      this.serviceCode = billCode;
      return this;
    }

    public String getServiceCode() {
      return serviceCode;
    }

    public AccountingInfo setServiceCode(String serviceCode) {
      this.serviceCode = serviceCode;
      return this;
    }

    public ServiceCode.Unit getUnit() {
      return unit;
    }

    public AccountingInfo setUnit(ServiceCode.Unit unit) {
      this.unit = unit;
      return this;
    }

    public double getUnitRate() {
      return unitRate;
    }

    public AccountingInfo setUnitRate(double unitRate) {
      this.unitRate = unitRate;
      return this;
    }

    @Deprecated
    public double getFlatRate() {
      return flatRate;
    }

    @Deprecated
    public AccountingInfo setFlatRate(double flatRate) {
      this.flatRate = flatRate;
      return this;
    }

    @Deprecated
    public double getHourlyRate() {
      return hourlyRate;
    }

    @Deprecated
    public AccountingInfo setHourlyRate(double hourlyRate) {
      this.hourlyRate = hourlyRate;
      return this;
    }

    @Deprecated
    public double getHours() {
      return hours;
    }

    @Deprecated
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

    @Deprecated
    public Double getMileage() {
      return null;
    }

    @Deprecated
    public AccountingInfo setMileage(double mileage) {
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
}
