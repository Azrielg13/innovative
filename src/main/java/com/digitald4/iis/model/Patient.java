package com.digitald4.iis.model;

import com.digitald4.common.model.Address;

public class Patient {
  private long id;
  private long referralDate;
  private long referralSourceId;
  private String referralSourceName;
  private long billingId;
  private String billingVendorName;
  private String name;
  private String mrNum;
  private long dateOfBirth;
  private long dianosisId;
  private long therapyTypeId;
  private long iVAccessId;
  private long startOfCardDate;
  private Address serviceAddress;
  private String phoneNumber;
  private long primaryPhoneTypdId;
  private String altContactNumber;
  private long altContactNumberTypeId;
  private String emergencyContact;
  private String emergencyContactPhone;
  private long eemergencyContactPhoneTypeId;
  private String rx;
  private long estLastDayOfService;
  private boolean labs;
  private String labsFrequency;
  private long firstRecertDue;
  private long dcDate;
  private boolean infoInSOS;
  private String schedulingPreference;
  private String referralNote;
  private long referralResolutionId;
  private long referralResolutionDate;
  private String referralResolutionNote;
  private long vendorConfirmationDate;
  private long nurseConfirmationDate;
  private long patientConfirmationDate;
  private long medsDeliveryDate;
  private long medsConfirmationDate;
  private boolean active;
  private String description;
  private double billingRate;
  private double billingRate2HrSoc;
  private double billingRate2HrRoc;
  private double billingFlat;
  private double billingFlat2HrSoc;
  private double billingFlat2HrRoc;
  private double mileageRate;
  private long patientStatusId;

  public long getId() {
    return id;
  }

  public Patient setId(long id) {
    this.id = id;
    return this;
  }

  public long getReferralDate() {
    return referralDate;
  }

  public Patient setReferralDate(long referralDate) {
    this.referralDate = referralDate;
    return this;
  }

  public long getReferralSourceId() {
    return referralSourceId;
  }

  public Patient setReferralSourceId(long referralSourceId) {
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

  public long getBillingId() {
    return billingId;
  }

  public Patient setBillingId(long billingId) {
    this.billingId = billingId;
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

  public long getDateOfBirth() {
    return dateOfBirth;
  }

  public Patient setDateOfBirth(long dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public long getDianosisId() {
    return dianosisId;
  }

  public Patient setDianosisId(long dianosisId) {
    this.dianosisId = dianosisId;
    return this;
  }

  public long getTherapyTypeId() {
    return therapyTypeId;
  }

  public Patient setTherapyTypeId(long therapyTypeId) {
    this.therapyTypeId = therapyTypeId;
    return this;
  }

  public long getiVAccessId() {
    return iVAccessId;
  }

  public Patient setiVAccessId(long iVAccessId) {
    this.iVAccessId = iVAccessId;
    return this;
  }

  public long getStartOfCardDate() {
    return startOfCardDate;
  }

  public Patient setStartOfCardDate(long startOfCardDate) {
    this.startOfCardDate = startOfCardDate;
    return this;
  }

  public Address getServiceAddress() {
    return serviceAddress;
  }

  public Patient setServiceAddress(Address serviceAddress) {
    this.serviceAddress = serviceAddress;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Patient setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public long getPrimaryPhoneTypdId() {
    return primaryPhoneTypdId;
  }

  public Patient setPrimaryPhoneTypdId(long primaryPhoneTypdId) {
    this.primaryPhoneTypdId = primaryPhoneTypdId;
    return this;
  }

  public String getAltContactNumber() {
    return altContactNumber;
  }

  public Patient setAltContactNumber(String altContactNumber) {
    this.altContactNumber = altContactNumber;
    return this;
  }

  public long getAltContactNumberTypeId() {
    return altContactNumberTypeId;
  }

  public Patient setAltContactNumberTypeId(long altContactNumberTypeId) {
    this.altContactNumberTypeId = altContactNumberTypeId;
    return this;
  }

  public String getEmergencyContact() {
    return emergencyContact;
  }

  public Patient setEmergencyContact(String emergencyContact) {
    this.emergencyContact = emergencyContact;
    return this;
  }

  public String getEmergencyContactPhone() {
    return emergencyContactPhone;
  }

  public Patient setEmergencyContactPhone(String emergencyContactPhone) {
    this.emergencyContactPhone = emergencyContactPhone;
    return this;
  }

  public long getEemergencyContactPhoneTypeId() {
    return eemergencyContactPhoneTypeId;
  }

  public Patient setEemergencyContactPhoneTypeId(long eemergencyContactPhoneTypeId) {
    this.eemergencyContactPhoneTypeId = eemergencyContactPhoneTypeId;
    return this;
  }

  public String getRx() {
    return rx;
  }

  public Patient setRx(String rx) {
    this.rx = rx;
    return this;
  }

  public long getEstLastDayOfService() {
    return estLastDayOfService;
  }

  public Patient setEstLastDayOfService(long estLastDayOfService) {
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

  public long getFirstRecertDue() {
    return firstRecertDue;
  }

  public Patient setFirstRecertDue(long firstRecertDue) {
    this.firstRecertDue = firstRecertDue;
    return this;
  }

  public long getDcDate() {
    return dcDate;
  }

  public Patient setDcDate(long dcDate) {
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

  public long getReferralResolutionId() {
    return referralResolutionId;
  }

  public Patient setReferralResolutionId(long referralResolutionId) {
    this.referralResolutionId = referralResolutionId;
    return this;
  }

  public long getReferralResolutionDate() {
    return referralResolutionDate;
  }

  public Patient setReferralResolutionDate(long referralResolutionDate) {
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

  public long getVendorConfirmationDate() {
    return vendorConfirmationDate;
  }

  public Patient setVendorConfirmationDate(long vendorConfirmationDate) {
    this.vendorConfirmationDate = vendorConfirmationDate;
    return this;
  }

  public long getNurseConfirmationDate() {
    return nurseConfirmationDate;
  }

  public Patient setNurseConfirmationDate(long nurseConfirmationDate) {
    this.nurseConfirmationDate = nurseConfirmationDate;
    return this;
  }

  public long getPatientConfirmationDate() {
    return patientConfirmationDate;
  }

  public Patient setPatientConfirmationDate(long patientConfirmationDate) {
    this.patientConfirmationDate = patientConfirmationDate;
    return this;
  }

  public long getMedsDeliveryDate() {
    return medsDeliveryDate;
  }

  public Patient setMedsDeliveryDate(long medsDeliveryDate) {
    this.medsDeliveryDate = medsDeliveryDate;
    return this;
  }

  public long getMedsConfirmationDate() {
    return medsConfirmationDate;
  }

  public Patient setMedsConfirmationDate(long medsConfirmationDate) {
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

  public double getBillingRate() {
    return billingRate;
  }

  public Patient setBillingRate(double billingRate) {
    this.billingRate = billingRate;
    return this;
  }

  public double getBillingRate2HrSoc() {
    return billingRate2HrSoc;
  }

  public Patient setBillingRate2HrSoc(double billingRate2HrSoc) {
    this.billingRate2HrSoc = billingRate2HrSoc;
    return this;
  }

  public double getBillingRate2HrRoc() {
    return billingRate2HrRoc;
  }

  public Patient setBillingRate2HrRoc(double billingRate2HrRoc) {
    this.billingRate2HrRoc = billingRate2HrRoc;
    return this;
  }

  public double getBillingFlat() {
    return billingFlat;
  }

  public Patient setBillingFlat(double billingFlat) {
    this.billingFlat = billingFlat;
    return this;
  }

  public double getBillingFlat2HrSoc() {
    return billingFlat2HrSoc;
  }

  public Patient setBillingFlat2HrSoc(double billingFlat2HrSoc) {
    this.billingFlat2HrSoc = billingFlat2HrSoc;
    return this;
  }

  public double getBillingFlat2HrRoc() {
    return billingFlat2HrRoc;
  }

  public Patient setBillingFlat2HrRoc(double billingFlat2HrRoc) {
    this.billingFlat2HrRoc = billingFlat2HrRoc;
    return this;
  }

  public double getMileageRate() {
    return mileageRate;
  }

  public Patient setMileageRate(double mileageRate) {
    this.mileageRate = mileageRate;
    return this;
  }

  public long getPatientStatusId() {
    return patientStatusId;
  }

  public Patient setPatientStatusId(long patientStatusId) {
    this.patientStatusId = patientStatusId;
    return this;
  }
}
