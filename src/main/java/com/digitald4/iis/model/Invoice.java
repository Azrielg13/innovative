package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;
import com.digitald4.common.model.ModelObjectModUser;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Invoice extends ModelObjectModUser<Long> {
  private long vendorId;
  private String vendorName;
  private String name;
  public enum Status {Unpaid, Partially_Paid, Paid, Cancelled}
  private Status status = Status.Unpaid;
  private Instant date;
  private Instant dueDate;
  private ImmutableList<Long> appointmentIds;
  private double loggedHours;
  private double standardBilling;
  private double mileage;
  private double billedMileage;
  private double totalDue;
  private double totalPaid;
  private String comment;
  private FileReference fileReference;

  public Invoice setId(Long id) {
    super.setId(id);
    return this;
  }

  public long getVendorId() {
    return vendorId;
  }

  public Invoice setVendorId(long vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  public String getVendorName() {
    return vendorName;
  }

  public Invoice setVendorName(String vendorName) {
    this.vendorName = vendorName;
    return this;
  }

  public String getName() {
    return name;
  }

  public Invoice setName(String name) {
    this.name = name;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Invoice setStatus(Status status) {
    this.status = status;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDate() {
    return date != null ? date : getCreationTime();
  }

  public Invoice setDate(Instant date) {
    this.date = date;
    return this;
  }

  @ApiResourceProperty
  public Long date() {
    return getDate() == null ? null : getDate().toEpochMilli();
  }

  public Invoice setDate(long date) {
    this.date = Instant.ofEpochMilli(date);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDueDate() {
    return dueDate != null ? dueDate : getDate().plus(30, ChronoUnit.DAYS);
  }

  public Invoice setDueDate(Instant dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  @ApiResourceProperty
  public Long dueDate() {
    return getDueDate() == null ? null : getDueDate().toEpochMilli();
  }

  public Invoice setDueDate(long date) {
    this.dueDate = Instant.ofEpochMilli(date);
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
}
