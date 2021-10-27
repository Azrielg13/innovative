package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;

public class Patient {
  private long id;
  private Long referralDate;
  private Long referralSourceId;
  private String referralSourceName;
  private long billingVendorId;
  private String billingVendorName;
  private String name;
  private String mrNum;
  private Long dateOfBirth;
  private Long dianosisId;
  private Long therapyTypeId;
  private Long ivAccessId;
  private Long startOfCareDate;
  private Address serviceAddress;
  private Phone primaryPhone;
  private Phone alternatePhone;
  private String emergencyContact;
  private Phone emergencyContactPhone;
  private String rx;
  private Long estLastDayOfService;
  private boolean labs;
  private String labsFrequency;
  private Long firstRecertDue;
  private Long dcDate;
  private boolean infoInSOS;
  private String schedulingPreference;
  private String referralNote;
  private Long referralResolutionId;
  private Long referralResolutionDate;
  private String referralResolutionNote;
  private Long vendorConfirmationDate;
  private Long nurseConfirmationDate;
  private Long patientConfirmationDate;
  private Long medsDeliveryDate;
  private Long medsConfirmationDate;
  private boolean active;
  private String description;
  private Double billingRate;
  private Double billingRate2HrSoc;
  private Double billingRate2HrRoc;
  private Double billingFlat;
  private Double billingFlat2HrSoc;
  private Double billingFlat2HrRoc;
  private Double mileageRate;
  private Long patientStatusId;

  public long getId() {
    return id;
  }

  public Patient setId(long id) {
    this.id = id;
    return this;
  }

  public Long getReferralDate() {
    return referralDate;
  }

  public Patient setReferralDate(Long referralDate) {
    this.referralDate = referralDate;
    return this;
  }

  public Long getReferralSourceId() {
    return referralSourceId;
  }

  public Patient setReferralSourceId(Long referralSourceId) {
    this.referralSourceId = referralSourceId;
    return this;
  }

  public String getReferralSourceName() {
    return referralSourceName;
  }

  public Patient setReferralSourceName(String referralSourceName) {
    this.referralSourceName = referralSourceName;
    return this;
  }

  public long getBillingVendorId() {
    return billingVendorId;
  }

  public Patient setBillingVendorId(long billingVendorId) {
    this.billingVendorId = billingVendorId;
    return this;
  }

  public String getBillingVendorName() {
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

  public Long getDateOfBirth() {
    return dateOfBirth;
  }

  public Patient setDateOfBirth(Long dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public Long getDianosisId() {
    return dianosisId;
  }

  public Patient setDianosisId(Long dianosisId) {
    this.dianosisId = dianosisId;
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

  public Long getStartOfCareDate() {
    return startOfCareDate;
  }

  public Patient setStartOfCareDate(Long startOfCareDate) {
    this.startOfCareDate = startOfCareDate;
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

  public Long getEstLastDayOfService() {
    return estLastDayOfService;
  }

  public Patient setEstLastDayOfService(Long estLastDayOfService) {
    this.estLastDayOfService = estLastDayOfService;
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

  public Long getFirstRecertDue() {
    return firstRecertDue;
  }

  public Patient setFirstRecertDue(Long firstRecertDue) {
    this.firstRecertDue = firstRecertDue;
    return this;
  }

  public Long getDcDate() {
    return dcDate;
  }

  public Patient setDcDate(Long dcDate) {
    this.dcDate = dcDate;
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

  public Long getReferralResolutionId() {
    return referralResolutionId;
  }

  public Patient setReferralResolutionId(Long referralResolutionId) {
    this.referralResolutionId = referralResolutionId;
    return this;
  }

  public Long getReferralResolutionDate() {
    return referralResolutionDate;
  }

  public Patient setReferralResolutionDate(Long referralResolutionDate) {
    this.referralResolutionDate = referralResolutionDate;
    return this;
  }

  public String getReferralResolutionNote() {
    return referralResolutionNote;
  }

  public Patient setReferralResolutionNote(String referralResolutionNote) {
    this.referralResolutionNote = referralResolutionNote;
    return this;
  }

  public Long getVendorConfirmationDate() {
    return vendorConfirmationDate;
  }

  public Patient setVendorConfirmationDate(Long vendorConfirmationDate) {
    this.vendorConfirmationDate = vendorConfirmationDate;
    return this;
  }

  public Long getNurseConfirmationDate() {
    return nurseConfirmationDate;
  }

  public Patient setNurseConfirmationDate(Long nurseConfirmationDate) {
    this.nurseConfirmationDate = nurseConfirmationDate;
    return this;
  }

  public Long getPatientConfirmationDate() {
    return patientConfirmationDate;
  }

  public Patient setPatientConfirmationDate(Long patientConfirmationDate) {
    this.patientConfirmationDate = patientConfirmationDate;
    return this;
  }

  public Long getMedsDeliveryDate() {
    return medsDeliveryDate;
  }

  public Patient setMedsDeliveryDate(Long medsDeliveryDate) {
    this.medsDeliveryDate = medsDeliveryDate;
    return this;
  }

  public Long getMedsConfirmationDate() {
    return medsConfirmationDate;
  }

  public Patient setMedsConfirmationDate(Long medsConfirmationDate) {
    this.medsConfirmationDate = medsConfirmationDate;
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

  public Long getPatientStatusId() {
    return patientStatusId;
  }

  public Patient setPatientStatusId(Long patientStatusId) {
    this.patientStatusId = patientStatusId;
    return this;
  }
}
