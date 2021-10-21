package com.digitald4.iis.model;

import com.digitald4.common.model.Address;

import static com.digitald4.common.util.Calculate.distance;
import static com.digitald4.common.util.Calculate.round;

public class Nurse {
  private long id;
  private String userName;
  private String email;
  private String firstName;
  private String lastName;
  private boolean disabled;
  private boolean readOnly;
  private String notes;
  private long lastLogin;
  private long reqDate;
  private Address address;
  private double payFlat;
  private double payRate;
  private double payFlat2HrRoc;
  private double payRate2HrRoc;
  private double payFlat2HrSoc;
  private double payRate2HrSoc;
  private double mileageRate;
  private long statusId;

  public long getId() {
    return id;
  }

  public Nurse setId(long id) {
    this.id = id;
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

  public String getFullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public void setFullName(String fullName) {}

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

  public long getLastLogin() {
    return lastLogin;
  }

  public Nurse setLastLogin(long lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  public long getReqDate() {
    return reqDate;
  }

  public Nurse setReqDate(long reqDate) {
    this.reqDate = reqDate;
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
      this.distance = round(distance(lat, lon, nurse.getAddress().getLatitude(), nurse.getAddress().getLongitude()), 1);
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
