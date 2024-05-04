package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Strings;

import java.time.Instant;

public class User extends IP360Entity implements com.digitald4.common.model.User, Employee {
  private int typeId;
  private String username;
  private String email;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private Status status = Status.Applicant;
  private Address address;
  private Instant dateOfBirth;
  private Instant hireDate;
  private String timeZone;
  private String jobTitle;
  private String department;
  private String notes;

  @Override
  public User setId(Long id) {
    super.setId(id);
    return this;
  }

  @Override
  public int getTypeId() {
    return typeId;
  }

  @Override
  public User setTypeId(int typeId) {
    this.typeId = typeId;
    return this;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public User setEmail(String email) {
    this.email = email;
    if (!Strings.isNullOrEmpty(email) && username == null) {
      this.username = email.substring(0, email.indexOf('@'));
    }
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public User setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  @ApiResourceProperty
  public String fullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public Status getStatus() {
    return status;
  }

  public User setStatus(Status status) {
    this.status = status;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public User setAddress(Address address) {
    this.address = address;
    return this;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public User setTimeZone(String timeZone) {
    this.timeZone = timeZone;
    return this;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public User setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
    return this;
  }

  public String getDepartment() {
    return department;
  }

  public User setDepartment(String department) {
    this.department = department;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDateOfBirth() {
    return dateOfBirth;
  }

  public User setDateOfBirth(Instant dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  @ApiResourceProperty
  public Long dateOfBirth() {
    return dateOfBirth == null ? null : dateOfBirth.toEpochMilli();
  }

  public User setDateOfBirth(long dateOfBirth) {
    this.dateOfBirth = Instant.ofEpochMilli(dateOfBirth);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getHireDate() {
    return hireDate;
  }

  public User setHireDate(Instant hireDate) {
    this.hireDate = hireDate;
    return this;
  }

  @ApiResourceProperty
  public Long hireDate() {
    return hireDate == null ? null : hireDate.toEpochMilli();
  }

  public User setHireDate(long hireDate) {
    this.hireDate = Instant.ofEpochMilli(hireDate);
    return this;
  }

  public String getNotes() {
    return notes;
  }

  public User setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public String toString() {
    return fullName();
  }
}
