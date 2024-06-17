package com.digitald4.iis.model;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Searchable;

public class Vendor extends IP360Entity implements Searchable {
  private String name;
  private Address address;
  private String phoneNumber;
  private String faxNumber;
  private String contactName;
  private String contactNumber;
  private String contactEmail;
  public enum Status {Active, In_Active, @Deprecated ACTIVE, @Deprecated IN_ACTIVE};
  private Status status = Status.Active;
  public enum InvoicingModel {Funder_Individual, Funder_Batched};
  private InvoicingModel invoicingModel;
  private double billingRate;
  private double billingRate2HrSoc;
  private double billingRate2HrRoc;
  private double billingFlat;
  private double billingFlat2HrSoc;
  private double billingFlat2HrRoc;
  private double mileageRate;
  private double holidayMultiplier;
  private String notes;

  public Vendor setId(Long id) {
    super.setId(id);
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

  public String getContactNumber() {
    return contactNumber;
  }

  public Vendor setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public Vendor setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }

  @Deprecated
  public Boolean isActive() {
    return null;
  }

  @Deprecated
  public Vendor setActive(boolean active) {
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Vendor setStatus(Status status) {
    this.status = switch (status) {
      case ACTIVE -> Status.Active;
      case IN_ACTIVE -> Status.In_Active;
      default -> status;
    };
    return this;
  }

  public InvoicingModel getInvoicingModel() {
    return invoicingModel;
  }

  public Vendor setInvoicingModel(InvoicingModel invoicingModel) {
    this.invoicingModel = invoicingModel;
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

  public double getHolidayMultiplier() {
    return holidayMultiplier;
  }

  public Vendor setHolidayMultiplier(double holidayMultiplier) {
    this.holidayMultiplier = holidayMultiplier;
    return this;
  }

  public String getNotes() {
    return notes;
  }

  public Vendor setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public String toString() {
    return getName();
  }
}
