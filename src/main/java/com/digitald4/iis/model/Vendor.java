package com.digitald4.iis.model;

import com.digitald4.common.model.Address;

public class Vendor {
  private long id;
  private String name;
  private Address address;
  private String phoneNumber;
  private String faxNumber;
  private String contactName;
  private String contactEmail;
  private boolean active = true;
  private double billingRate;
  private double billingRate2HrSoc;
  private double billingRate2HrRoc;
  private double billingFlat;
  private double billingFlat2HrSoc;
  private double billingFlat2HrRoc;
  private double mileageRate;
  private String notes;

  public long getId() {
    return id;
  }

  public Vendor setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Vendor setName(String name) {
    this.name = name;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Vendor setAddress(Address address) {
    this.address = address;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Vendor setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public String getFaxNumber() {
    return faxNumber;
  }

  public Vendor setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
    return this;
  }

  public String getContactName() {
    return contactName;
  }

  public Vendor setContactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public Vendor setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }

  public boolean isActive() {
    return active;
  }

  public Vendor setActive(boolean active) {
    this.active = active;
    return this;
  }

  public double getBillingRate() {
    return billingRate;
  }

  public Vendor setBillingRate(double billingRate) {
    this.billingRate = billingRate;
    return this;
  }

  public double getBillingRate2HrSoc() {
    return billingRate2HrSoc;
  }

  public Vendor setBillingRate2HrSoc(double billingRate2HrSoc) {
    this.billingRate2HrSoc = billingRate2HrSoc;
    return this;
  }

  public double getBillingRate2HrRoc() {
    return billingRate2HrRoc;
  }

  public Vendor setBillingRate2HrRoc(double billingRate2HrRoc) {
    this.billingRate2HrRoc = billingRate2HrRoc;
    return this;
  }

  public double getBillingFlat() {
    return billingFlat;
  }

  public Vendor setBillingFlat(double billingFlat) {
    this.billingFlat = billingFlat;
    return this;
  }

  public double getBillingFlat2HrSoc() {
    return billingFlat2HrSoc;
  }

  public Vendor setBillingFlat2HrSoc(double billingFlat2HrSoc) {
    this.billingFlat2HrSoc = billingFlat2HrSoc;
    return this;
  }

  public double getBillingFlat2HrRoc() {
    return billingFlat2HrRoc;
  }

  public Vendor setBillingFlat2HrRoc(double billingFlat2HrRoc) {
    this.billingFlat2HrRoc = billingFlat2HrRoc;
    return this;
  }

  public double getMileageRate() {
    return mileageRate;
  }

  public Vendor setMileageRate(double mileageRate) {
    this.mileageRate = mileageRate;
    return this;
  }

  public String getNotes() {
    return notes;
  }

  public Vendor setNotes(String notes) {
    this.notes = notes;
    return this;
  }
}
