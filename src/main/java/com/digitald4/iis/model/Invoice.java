package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;
import com.google.common.collect.ImmutableList;

public class Invoice {
  private long id;
  private long vendorId;
  private String name;
  private long generationTime;
  private long statusId;
  private ImmutableList<Long> appointmentIds;
  private double loggedHours;
  private double standardBilling;
  private double mileage;
  private double billedMileage;
  private double totalDue;
  private double totalPaid;
  private String comment;
  private FileReference fileReference;
  private double loggedHoursYTD;
  private double standardBillingYTD;
  private double mileageYTD;
  private double billedMileageYTD;
  private double billedYTD;

  public long getId() {
    return id;
  }

  public Invoice setId(long id) {
    this.id = id;
    return this;
  }

  public long getVendorId() {
    return vendorId;
  }

  public Invoice setVendorId(long vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  public String getName() {
    return name;
  }

  public Invoice setName(String name) {
    this.name = name;
    return this;
  }

  public long getGenerationTime() {
    return generationTime;
  }

  public Invoice setGenerationTime(long generationTime) {
    this.generationTime = generationTime;
    return this;
  }

  public long getStatusId() {
    return statusId;
  }

  public Invoice setStatusId(long statusId) {
    this.statusId = statusId;
    return this;
  }

  public ImmutableList<Long> getAppointmentIds() {
    return appointmentIds;
  }

  public Invoice setAppointmentIds(Iterable<Long> appointmentIds) {
    this.appointmentIds = ImmutableList.copyOf(appointmentIds);
    return this;
  }

  public double getLoggedHours() {
    return loggedHours;
  }

  public Invoice setLoggedHours(double loggedHours) {
    this.loggedHours = loggedHours;
    return this;
  }

  public double getStandardBilling() {
    return standardBilling;
  }

  public Invoice setStandardBilling(double standardBilling) {
    this.standardBilling = standardBilling;
    return this;
  }

  public double getMileage() {
    return mileage;
  }

  public Invoice setMileage(double mileage) {
    this.mileage = mileage;
    return this;
  }

  public double getBilledMileage() {
    return billedMileage;
  }

  public Invoice setBilledMileage(double billedMileage) {
    this.billedMileage = billedMileage;
    return this;
  }

  public double getTotalDue() {
    return totalDue;
  }

  public Invoice setTotalDue(double totalDue) {
    this.totalDue = totalDue;
    return this;
  }

  public double getTotalPaid() {
    return totalPaid;
  }

  public Invoice setTotalPaid(double totalPaid) {
    this.totalPaid = totalPaid;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public Invoice setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public Invoice setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
    return this;
  }

  public double getLoggedHoursYTD() {
    return loggedHoursYTD;
  }

  public Invoice setLoggedHoursYTD(double loggedHoursYTD) {
    this.loggedHoursYTD = loggedHoursYTD;
    return this;
  }

  public double getStandardBillingYTD() {
    return standardBillingYTD;
  }

  public Invoice setStandardBillingYTD(double standardBillingYTD) {
    this.standardBillingYTD = standardBillingYTD;
    return this;
  }

  public double getMileageYTD() {
    return mileageYTD;
  }

  public Invoice setMileageYTD(double mileageYTD) {
    this.mileageYTD = mileageYTD;
    return this;
  }

  public double getBilledMileageYTD() {
    return billedMileageYTD;
  }

  public Invoice setBilledMileageYTD(double billedMileageYTD) {
    this.billedMileageYTD = billedMileageYTD;
    return this;
  }

  public double getBilledYTD() {
    return billedYTD;
  }

  public Invoice setBilledYTD(double billedYTD) {
    this.billedYTD = billedYTD;
    return this;
  }
}
