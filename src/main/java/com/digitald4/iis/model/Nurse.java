package com.digitald4.iis.model;

import static com.digitald4.common.util.Calculate.distance;
import static com.digitald4.common.util.Calculate.round;

import com.digitald4.common.model.Address;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;

public class Nurse extends IP360Entity implements Employee {
  private String userName;
  private String email;
  private String firstName;
  private String lastName;
  private Boolean disabled;
  private Boolean readOnly;
  private String notes;
  private Instant dateOfBirth;
  private Instant regDate;
  private Instant hireDate;
  private Address address;
  private String phoneNumber;
  private Double payFlat;
  private Double payRate;
  private Double payFlat2HrRoc;
  private Double payRate2HrRoc;
  private Double payFlat2HrSoc;
  private Double payRate2HrSoc;
  private Double mileageRate;
  private Status status = Status.Applicant;
  private String timeZone;

  public Nurse setId(Long id) {
    super.setId(id);
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Nurse setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Nurse setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getUserName() {
    return userName;
  }

  @ApiResourceProperty
  public String fullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public Nurse setUserName(String userName) {
    this.userName = userName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Nurse setEmail(String email) {
    this.email = email;
    return this;
  }

  public Boolean isDisabled() {
    return disabled;
  }

  public Nurse setDisabled(Boolean disabled) {
    this.disabled = disabled;
    return this;
  }

  public Boolean isReadOnly() {
    return readOnly;
  }

  public Nurse setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

  public String getNotes() {
    return notes;
  }

  public Nurse setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDateOfBirth() {
    return dateOfBirth;
  }

  public Nurse setDateOfBirth(Instant dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  @ApiResourceProperty
  public Long dateOfBirth() {
    return dateOfBirth == null ? null : dateOfBirth.toEpochMilli();
  }

  public Nurse setDateOfBirth(long dateOfBirth) {
    this.dateOfBirth = Instant.ofEpochMilli(dateOfBirth);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getRegDate() {
    return regDate;
  }

  public Nurse setRegDate(Instant regDate) {
    this.regDate = regDate;
    return this;
  }

  @ApiResourceProperty
  public Long regDate() {
    return regDate == null ? null : regDate.toEpochMilli();
  }

  public Nurse setRegDate(long regDate) {
    this.regDate = Instant.ofEpochMilli(regDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getHireDate() {
    return hireDate;
  }

  public Nurse setHireDate(Instant hireDate) {
    this.hireDate = hireDate;
    return this;
  }

  @ApiResourceProperty
  public Long hireDate() {
    return hireDate == null ? null : hireDate.toEpochMilli();
  }

  public Nurse setHireDate(long hireDate) {
    this.hireDate = Instant.ofEpochMilli(hireDate);
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Nurse setStatus(Status status) {
    this.status = status;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Nurse setAddress(Address address) {
    this.address = address;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Nurse setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public Double getPayFlat() {
    return payFlat;
  }

  public Nurse setPayFlat(Double payFlat) {
    this.payFlat = payFlat;
    return this;
  }

  public Double getPayRate() {
    return payRate;
  }

  public Nurse setPayRate(Double payRate) {
    this.payRate = payRate;
    return this;
  }

  public Double getPayFlat2HrRoc() {
    return payFlat2HrRoc;
  }

  public Nurse setPayFlat2HrRoc(Double payFlat2HrRoc) {
    this.payFlat2HrRoc = payFlat2HrRoc;
    return this;
  }

  public Double getPayRate2HrRoc() {
    return payRate2HrRoc;
  }

  public Nurse setPayRate2HrRoc(Double payRate2HrRoc) {
    this.payRate2HrRoc = payRate2HrRoc;
    return this;
  }

  public Double getPayFlat2HrSoc() {
    return payFlat2HrSoc;
  }

  public Nurse setPayFlat2HrSoc(Double payFlat2HrSoc) {
    this.payFlat2HrSoc = payFlat2HrSoc;
    return this;
  }

  public Double getPayRate2HrSoc() {
    return payRate2HrSoc;
  }

  public Nurse setPayRate2HrSoc(Double payRate2HrSoc) {
    this.payRate2HrSoc = payRate2HrSoc;
    return this;
  }

  public Double getMileageRate() {
    return mileageRate;
  }

  public Nurse setMileageRate(Double mileageRate) {
    this.mileageRate = mileageRate;
    return this;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public Nurse setTimeZone(String timeZone) {
    this.timeZone = timeZone;
    return this;
  }

  public String toString() {
    return fullName();
  }

  public static class DistanceNurse {
    private final Double distance;
    private final Nurse nurse;

    public DistanceNurse(Double lat, Double lon, Nurse nurse) {
      double nurseLat = 0.;
      double nurseLon = 0;
      if (nurse.getAddress() != null) {
        nurseLat = nurse.getAddress().getLatitude();
        nurseLon = nurse.getAddress().getLongitude();
      }
      this.distance = round(distance(lat, lon, nurseLat, nurseLon), 1);
      this.nurse = nurse;
    }

    public Double getDistance() {
      return distance;
    }

    public Nurse getNurse() {
      return nurse;
    }
  }
}
