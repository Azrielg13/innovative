package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.digitald4.iis.storage.GenData;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;

import static com.digitald4.common.util.Calculate.distance;
import static com.digitald4.common.util.Calculate.round;

public class Nurse extends IP360Entity {
  private String userName;
  private String email;
  private String firstName;
  private String lastName;
  private boolean disabled;
  private boolean readOnly;
  private String notes;
  private Instant regDate;
  private Address address;
  private String phoneNumber;
  private double payFlat;
  private double payRate;
  private double payFlat2HrRoc;
  private double payRate2HrRoc;
  private double payFlat2HrSoc;
  private double payRate2HrSoc;
  private double mileageRate;
  private long statusId = GenData.NURSE_STATUS_PENDING;

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

  @ApiResourceProperty
  public String fullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public String getUserName() {
    return userName;
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

  public boolean isDisabled() {
    return disabled;
  }

  public Nurse setDisabled(boolean disabled) {
    this.disabled = disabled;
    return this;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public Nurse setReadOnly(boolean readOnly) {
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

  public long getStatusId() {
    return statusId;
  }

  public Nurse setStatusId(long statusId) {
    this.statusId = statusId;
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

  public double getPayFlat() {
    return payFlat;
  }

  public Nurse setPayFlat(double payFlat) {
    this.payFlat = payFlat;
    return this;
  }

  public double getPayRate() {
    return payRate;
  }

  public Nurse setPayRate(double payRate) {
    this.payRate = payRate;
    return this;
  }

  public double getPayFlat2HrRoc() {
    return payFlat2HrRoc;
  }

  public Nurse setPayFlat2HrRoc(double payFlat2HrRoc) {
    this.payFlat2HrRoc = payFlat2HrRoc;
    return this;
  }

  public double getPayRate2HrRoc() {
    return payRate2HrRoc;
  }

  public Nurse setPayRate2HrRoc(double payRate2HrRoc) {
    this.payRate2HrRoc = payRate2HrRoc;
    return this;
  }

  public double getPayFlat2HrSoc() {
    return payFlat2HrSoc;
  }

  public Nurse setPayFlat2HrSoc(double payFlat2HrSoc) {
    this.payFlat2HrSoc = payFlat2HrSoc;
    return this;
  }

  public double getPayRate2HrSoc() {
    return payRate2HrSoc;
  }

  public Nurse setPayRate2HrSoc(double payRate2HrSoc) {
    this.payRate2HrSoc = payRate2HrSoc;
    return this;
  }

  public double getMileageRate() {
    return mileageRate;
  }

  public Nurse setMileageRate(double mileageRate) {
    this.mileageRate = mileageRate;
    return this;
  }

  public static class DistanceNurse {
    private final double distance;
    private final Nurse nurse;

    public DistanceNurse(double lat, double lon, Nurse nurse) {
      double nurseLat = 0;
      double nurseLon = 0;
      if (nurse.getAddress() != null) {
        nurseLat = nurse.getAddress().getLatitude();
        nurseLon = nurse.getAddress().getLongitude();
      }
      this.distance = round(distance(lat, lon, nurseLat, nurseLon), 1);
      this.nurse = nurse;
    }

    public double getDistance() {
      return distance;
    }

    public Nurse getNurse() {
      return nurse;
    }
  }
}
