package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.common.model.Searchable;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patient extends IP360Entity implements Searchable {
  private Instant referralDate;
  private String referralSource;
  private Long billingVendorId;
  private String billingVendorName;
  private String firstName;
  private String lastName;
  private String mrNum;
  private Instant dateOfBirth;
  private String diagnosis;
  private String therapyType;
  private String ivAccess;
  private String ivType;
  private String ivPumpBrand;
  private Instant startOfCareDate;
  private Address serviceAddress;
  private String email;
  private Phone phonePrimary;
  private Phone phoneAlternate;
  private Phone phonePersonal;
  private String emergencyContact;
  private Phone emergencyContactPhone;
  private String preferredLanguage;
  private String timeZone;
  private String rx;
  private Instant estLastDayOfService;
  private boolean labs;
  private String labsFrequency;
  private Instant firstRecertDue;
  private Instant dcDate;
  private boolean infoInSOS;
  private String schedulingPreference;
  private String referralNote;
  public enum Status {Active, Denied, Discharged, Hospitalized, On_Hold, Pending, Vacation, Waiting_For_Authorization}
  private Status status = Status.Pending;
  private Instant referralResolutionDate;
  private String referralResolutionNote;
  private Instant vendorConfirmationDate;
  private Instant nurseConfirmationDate;
  private Instant patientConfirmationDate;
  private Instant medsDeliveryDate;
  private Instant medsConfirmationDate;
  private String description;
  private Double billingRate;
  private Double billingRate2HrSoc;
  private Double billingRate2HrRoc;
  private Double billingFlat;
  private Double billingFlat2HrSoc;
  private Double billingFlat2HrRoc;
  private Double mileageRate;
  private String visitType;
  private Long visitTypeId;
  private String titration;
  public enum VisitFrequency {ONE_TIME, MANAGED};
  private VisitFrequency visitFrequency;

  public enum Gender {Male, Female, Other, Unspecified};
  private Gender gender;

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

  @Deprecated
  public Long getReferralSourceId() {
    return null;
  }

  @Deprecated
  public Patient setReferralSourceId(Long referralSourceId) {
    return this;
  }

  @Deprecated
  public String getReferralSourceName() {
    return null;
  }

  @Deprecated
  public Patient setReferralSourceName(String referralSourceName) {
    this.referralSource = referralSourceName;
    return this;
  }

  public String getReferralSource() {
    return referralSource;
  }

  public Patient setReferralSource(String referralSource) {
    this.referralSource = referralSource;
    return this;
  }

  public Long getBillingVendorId() {
    return billingVendorId;
  }

  public Patient setBillingVendorId(Long billingVendorId) {
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

  private static final Pattern NAME_PATTERN = Pattern.compile("([A-z]+) ([A-z- ]+)");
  @Deprecated
  public String getName() {
    return null;
  }

  @Deprecated
  public Patient setName(String name) {
    name = name.trim();
    Matcher matcher = NAME_PATTERN.matcher(name);
    if (firstName == null && lastName == null && matcher.matches()) {
      this.firstName = matcher.group(1);
      this.lastName = matcher.group(2);
    }
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Patient setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Patient setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @ApiResourceProperty
  public String fullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public String getMrNum() {
    return mrNum;
  }

  public Patient setMrNum(String mrNum) {
    this.mrNum = mrNum;
    return this;
  }

  public Gender getGender() {
    return gender;
  }

  public Patient setGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public Patient setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
    return this;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public Patient setTimeZone(String timeZone) {
    this.timeZone = timeZone;
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

  @Deprecated
  public Long getDiagnosisId() {
    return null;
  }

  @Deprecated
  public Patient setDiagnosisId(Long diagnosisId) {
    return this;
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  public Patient setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
    return this;
  }

  public String getTherapyType() {
    return therapyType;
  }

  public Patient setTherapyType(String therapyType) {
    this.therapyType = therapyType;
    return this;
  }

  @Deprecated
  public Long getTherapyTypeId() {
    return null;
  }

  @Deprecated
  public Patient setTherapyTypeId(Long therapyTypeId) {
    return this;
  }

  @Deprecated
  public Long getIvAccessId() {
    return null;
  }

  @Deprecated
  public Patient setIvAccessId(Long ivAccessId) {
    return this;
  }

  public String getIvAccess() {
    return ivAccess;
  }

  public Patient setIvAccess(String ivAccess) {
    this.ivAccess = ivAccess;
    return this;
  }

  public String getIvType() {
    return ivType;
  }

  public Patient setIvType(String ivType) {
    this.ivType = ivType;
    return this;
  }

  public String getIvPumpBrand() {
    return ivPumpBrand;
  }

  public Patient setIvPumpBrand(String ivPumpBrand) {
    this.ivPumpBrand = ivPumpBrand;
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

  public String getEmail() {
    return email;
  }

  public Patient setEmail(String email) {
    this.email = email;
    return this;
  }

  public Phone getPhonePrimary() {
    return phonePrimary;
  }

  public Patient setPhonePrimary(Phone phonePrimary) {
    this.phonePrimary = phonePrimary;
    return this;
  }

  public Phone getPhoneAlternate() {
    return phoneAlternate;
  }

  public Patient setPhoneAlternate(Phone phoneAlternate) {
    this.phoneAlternate = phoneAlternate;
    return this;
  }

  public Phone getPhonePersonal() {
    return phonePersonal;
  }

  public Patient setPhonePersonal(Phone phonePersonal) {
    this.phonePersonal = phonePersonal;
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

  public Status getStatus() {
    return status;
  }

  public Patient setStatus(Status status) {
    this.status = status;
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

  public String getVisitType() {
    return visitType;
  }

  public Patient setVisitType(String visitType) {
    this.visitType = visitType;
    return this;
  }

  public Long getVisitTypeId() {
    return visitTypeId;
  }

  public Patient setVisitTypeId(Long visitTypeId) {
    this.visitTypeId = visitTypeId;
    return this;
  }

  public String getTitration() {
    return titration;
  }

  public Patient setTitration(String titration) {
    this.titration = titration;
    return this;
  }

  public VisitFrequency getVisitFrequency() {
    return visitFrequency;
  }

  public Patient setVisitFrequency(VisitFrequency visitFrequency) {
    this.visitFrequency = visitFrequency;
    return this;
  }

  @Override
  @ApiResourceProperty
  public String toString() {
    return fullName();
  }
}
