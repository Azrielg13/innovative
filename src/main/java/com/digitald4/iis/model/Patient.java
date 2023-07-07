package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.iis.storage.GenData;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;

public class Patient extends IP360Entity {
  private Instant referralDate;
  private Long referralSourceId;
  private String referralSourceName;
  private Long billingVendorId;
  private String billingVendorName;
  private String name;
  private String mrNum;
  private Instant dateOfBirth;
  private Long diagnosisId;
  private Long therapyTypeId;
  private Long ivAccessId;
  private Instant startOfCareDate;
  private Address serviceAddress;
  private Phone primaryPhone;
  private Phone alternatePhone;
  private String emergencyContact;
  private Phone emergencyContactPhone;
  private String rx;
  private Instant estLastDayOfService;
  private boolean labs;
  private String labsFrequency;
  private Instant firstRecertDue;
  private Instant dcDate;
  private boolean infoInSOS;
  private String schedulingPreference;
  private String referralNote;
  private Long statusId = GenData.PATIENT_STATE_PENDING;
  private Instant referralResolutionDate;
  private String referralResolutionNote;
  private Instant vendorConfirmationDate;
  private Instant nurseConfirmationDate;
  private Instant patientConfirmationDate;
  private Instant medsDeliveryDate;
  private Instant medsConfirmationDate;
  private boolean active;
  private String description;
  private Double billingRate;
  private Double billingRate2HrSoc;
  private Double billingRate2HrRoc;
  private Double billingFlat;
  private Double billingFlat2HrSoc;
  private Double billingFlat2HrRoc;
  private Double mileageRate;
  private Long typeId;

  public Patient setId(Long id) {
    super.setId(id);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getReferralDate() {
    return referralDate;
  }

  public Patient setReferralDate(Instant referralDate) {
    this.referralDate = referralDate;
    return this;
  }

  @ApiResourceProperty
  public Long referralDate() {
    return referralDate == null ? null : referralDate.toEpochMilli();
  }

  public Patient setReferralDate(long referralDate) {
    this.referralDate = Instant.ofEpochMilli(referralDate);
    return this;
  }

  public Long getReferralSourceId() {
    return referralSourceId;
  }

  public Patient setReferralSourceId(Long referralSourceId) {
    this.referralSourceId = referralSourceId;
    return this;
  }

  @ApiResourceProperty
  public String referralSourceName() {
    return referralSourceName;
  }

  public Patient setReferralSourceName(String referralSourceName) {
    this.referralSourceName = referralSourceName;
    return this;
  }

  public Long getBillingVendorId() {
    return billingVendorId;
  }

  public Patient setBillingVendorId(Long billingVendorId) {
    this.billingVendorId = billingVendorId;
    return this;
  }

  @ApiResourceProperty
  public String billingVendorName() {
    return billingVendorName;
  }

  public Patient setBillingVendorName(String billingVendorName) {
    this.billingVendorName = billingVendorName;
    return this;
  }

  public String getName() {
    return name;
  }

  public Patient setName(String name) {
    this.name = name;
    return this;
  }

  public String getMrNum() {
    return mrNum;
  }

  public Patient setMrNum(String mrNum) {
    this.mrNum = mrNum;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDateOfBirth() {
    return dateOfBirth;
  }

  public Patient setDateOfBirth(Instant dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  @ApiResourceProperty
  public Long dateOfBirth() {
    return dateOfBirth == null ? null : dateOfBirth.toEpochMilli();
  }

  public Patient setDateOfBirth(long dateOfBirth) {
    this.dateOfBirth = Instant.ofEpochMilli(dateOfBirth);
    return this;
  }

  public Long getDiagnosisId() {
    return diagnosisId;
  }

  public Patient setDiagnosisId(Long diagnosisId) {
    this.diagnosisId = diagnosisId;
    return this;
  }

  public Long getTherapyTypeId() {
    return therapyTypeId;
  }

  public Patient setTherapyTypeId(Long therapyTypeId) {
    this.therapyTypeId = therapyTypeId;
    return this;
  }

  public Long getIvAccessId() {
    return ivAccessId;
  }

  public Patient setIvAccessId(Long ivAccessId) {
    this.ivAccessId = ivAccessId;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getStartOfCareDate() {
    return startOfCareDate;
  }

  public Patient setStartOfCareDate(Instant startOfCareDate) {
    this.startOfCareDate = startOfCareDate;
    return this;
  }

  @ApiResourceProperty
  public Long startOfCareDate() {
    return startOfCareDate == null ? null : startOfCareDate.toEpochMilli();
  }

  public Patient setStartOfCareDate(long startOfCareDate) {
    this.startOfCareDate = Instant.ofEpochMilli(startOfCareDate);
    return this;
  }

  public Address getServiceAddress() {
    return serviceAddress;
  }

  public Patient setServiceAddress(Address serviceAddress) {
    this.serviceAddress = serviceAddress;
    return this;
  }

  public Phone getPrimaryPhone() {
    return primaryPhone;
  }

  public Patient setPrimaryPhone(Phone primaryPhone) {
    this.primaryPhone = primaryPhone;
    return this;
  }

  public Phone getAlternatePhone() {
    return alternatePhone;
  }

  public Patient setAlternatePhone(Phone alternatePhone) {
    this.alternatePhone = alternatePhone;
    return this;
  }

  public String getEmergencyContact() {
    return emergencyContact;
  }

  public Patient setEmergencyContact(String emergencyContact) {
    this.emergencyContact = emergencyContact;
    return this;
  }

  public Phone getEmergencyContactPhone() {
    return emergencyContactPhone;
  }

  public Patient setEmergencyContactPhone(Phone emergencyContactPhone) {
    this.emergencyContactPhone = emergencyContactPhone;
    return this;
  }

  public String getRx() {
    return rx;
  }

  public Patient setRx(String rx) {
    this.rx = rx;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getEstLastDayOfService() {
    return estLastDayOfService;
  }

  public Patient setEstLastDayOfService(Instant estLastDayOfService) {
    this.estLastDayOfService = estLastDayOfService;
    return this;
  }

  @ApiResourceProperty
  public Long estLastDayOfService() {
    return estLastDayOfService == null ? null : estLastDayOfService.toEpochMilli();
  }

  public Patient setEstLastDayOfService(long estLastDayOfService) {
    this.estLastDayOfService = Instant.ofEpochMilli(estLastDayOfService);
    return this;
  }

  public boolean isLabs() {
    return labs;
  }

  public Patient setLabs(boolean labs) {
    this.labs = labs;
    return this;
  }

  public String getLabsFrequency() {
    return labsFrequency;
  }

  public Patient setLabsFrequency(String labsFrequency) {
    this.labsFrequency = labsFrequency;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getFirstRecertDue() {
    return firstRecertDue;
  }

  public Patient setFirstRecertDue(Instant firstRecertDue) {
    this.firstRecertDue = firstRecertDue;
    return this;
  }

  @ApiResourceProperty
  public Long firstRecertDue() {
    return firstRecertDue == null ? null : firstRecertDue.toEpochMilli();
  }

  public Patient setFirstRecertDue(long firstRecertDue) {
    this.firstRecertDue = Instant.ofEpochMilli(firstRecertDue);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDcDate() {
    return dcDate;
  }

  public Patient setDcDate(Instant dcDate) {
    this.dcDate = dcDate;
    return this;
  }

  @ApiResourceProperty
  public Long dcDate() {
    return dcDate == null ? null : dcDate.toEpochMilli();
  }

  public Patient setDcDate(long dcDate) {
    this.dcDate = Instant.ofEpochMilli(dcDate);
    return this;
  }

  public boolean isInfoInSOS() {
    return infoInSOS;
  }

  public Patient setInfoInSOS(boolean infoInSOS) {
    this.infoInSOS = infoInSOS;
    return this;
  }

  public String getSchedulingPreference() {
    return schedulingPreference;
  }

  public Patient setSchedulingPreference(String schedulingPreference) {
    this.schedulingPreference = schedulingPreference;
    return this;
  }

  public String getReferralNote() {
    return referralNote;
  }

  public Patient setReferralNote(String referralNote) {
    this.referralNote = referralNote;
    return this;
  }

  public Long getStatusId() {
    return statusId;
  }

  public Patient setStatusId(Long statusId) {
    this.statusId = statusId;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getReferralResolutionDate() {
    return referralResolutionDate;
  }

  public Patient setReferralResolutionDate(Instant referralResolutionDate) {
    this.referralResolutionDate = referralResolutionDate;
    return this;
  }

  @ApiResourceProperty
  public Long referralResolutionDate() {
    return referralResolutionDate == null ? null : referralResolutionDate.toEpochMilli();
  }

  public Patient setReferralResolutionDate(long referralResolutionDate) {
    this.referralResolutionDate = Instant.ofEpochMilli(referralResolutionDate);
    return this;
  }

  public String getReferralResolutionNote() {
    return referralResolutionNote;
  }

  public Patient setReferralResolutionNote(String referralResolutionNote) {
    this.referralResolutionNote = referralResolutionNote;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getVendorConfirmationDate() {
    return vendorConfirmationDate;
  }

  public Patient setVendorConfirmationDate(Instant vendorConfirmationDate) {
    this.vendorConfirmationDate = vendorConfirmationDate;
    return this;
  }

  @ApiResourceProperty
  public Long vendorConfirmationDate() {
    return vendorConfirmationDate == null ? null : vendorConfirmationDate.toEpochMilli();
  }

  public Patient setVendorConfirmationDate(long vendorConfirmationDate) {
    this.vendorConfirmationDate = Instant.ofEpochMilli(vendorConfirmationDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getNurseConfirmationDate() {
    return nurseConfirmationDate;
  }

  public Patient setNurseConfirmationDate(Instant nurseConfirmationDate) {
    this.nurseConfirmationDate = nurseConfirmationDate;
    return this;
  }

  @ApiResourceProperty
  public Long nurseConfirmationDate() {
    return nurseConfirmationDate == null ? null : nurseConfirmationDate.toEpochMilli();
  }

  public Patient setNurseConfirmationDate(long nurseConfirmationDate) {
    this.nurseConfirmationDate = Instant.ofEpochMilli(nurseConfirmationDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getPatientConfirmationDate() {
    return patientConfirmationDate;
  }

  public Patient setPatientConfirmationDate(Instant patientConfirmationDate) {
    this.patientConfirmationDate = patientConfirmationDate;
    return this;
  }

  @ApiResourceProperty
  public Long patientConfirmationDate() {
    return patientConfirmationDate == null ? null : patientConfirmationDate.toEpochMilli();
  }

  public Patient setPatientConfirmationDate(long patientConfirmationDate) {
    this.patientConfirmationDate = Instant.ofEpochMilli(patientConfirmationDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getMedsDeliveryDate() {
    return medsDeliveryDate;
  }

  public Patient setMedsDeliveryDate(Instant medsDeliveryDate) {
    this.medsDeliveryDate = medsDeliveryDate;
    return this;
  }

  @ApiResourceProperty
  public Long medsDeliveryDate() {
    return medsDeliveryDate == null ? null : medsDeliveryDate.toEpochMilli();
  }

  public Patient setMedsDeliveryDate(long medsDeliveryDate) {
    this.medsDeliveryDate = Instant.ofEpochMilli(medsDeliveryDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getMedsConfirmationDate() {
    return medsConfirmationDate;
  }

  public Patient setMedsConfirmationDate(Instant medsConfirmationDate) {
    this.medsConfirmationDate = medsConfirmationDate;
    return this;
  }

  @ApiResourceProperty
  public Long medsConfirmationDate() {
    return medsConfirmationDate == null ? null : medsConfirmationDate.toEpochMilli();
  }

  public Patient setMedsConfirmationDate(long medsConfirmationDate) {
    this.medsConfirmationDate = Instant.ofEpochMilli(medsConfirmationDate);
    return this;
  }

  public boolean isActive() {
    return active;
  }

  public Patient setActive(boolean active) {
    this.active = active;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Patient setDescription(String description) {
    this.description = description;
    return this;
  }

  public Double getBillingRate() {
    return billingRate;
  }

  public Patient setBillingRate(Double billingRate) {
    this.billingRate = billingRate;
    return this;
  }

  public Double getBillingRate2HrSoc() {
    return billingRate2HrSoc;
  }

  public Patient setBillingRate2HrSoc(Double billingRate2HrSoc) {
    this.billingRate2HrSoc = billingRate2HrSoc;
    return this;
  }

  public Double getBillingRate2HrRoc() {
    return billingRate2HrRoc;
  }

  public Patient setBillingRate2HrRoc(Double billingRate2HrRoc) {
    this.billingRate2HrRoc = billingRate2HrRoc;
    return this;
  }

  public Double getBillingFlat() {
    return billingFlat;
  }

  public Patient setBillingFlat(Double billingFlat) {
    this.billingFlat = billingFlat;
    return this;
  }

  public Double getBillingFlat2HrSoc() {
    return billingFlat2HrSoc;
  }

  public Patient setBillingFlat2HrSoc(Double billingFlat2HrSoc) {
    this.billingFlat2HrSoc = billingFlat2HrSoc;
    return this;
  }

  public Double getBillingFlat2HrRoc() {
    return billingFlat2HrRoc;
  }

  public Patient setBillingFlat2HrRoc(Double billingFlat2HrRoc) {
    this.billingFlat2HrRoc = billingFlat2HrRoc;
    return this;
  }

  public Double getMileageRate() {
    return mileageRate;
  }

  public Patient setMileageRate(Double mileageRate) {
    this.mileageRate = mileageRate;
    return this;
  }

  public Long getTypeId() {
    return typeId;
  }

  public Patient setTypeId(Long typeId) {
    this.typeId = typeId;
    return this;
  }
}
