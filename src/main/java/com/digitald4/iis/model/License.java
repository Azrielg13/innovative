package com.digitald4.iis.model;

import com.digitald4.common.model.ChangeTrackable;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.model.ModelObject;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;

public class License extends ModelObject<String> implements ChangeTrackable<String> {
  private long nurseId;
  private String nurseName;
  private Employee.Status nurseStatus;
  private long licTypeId;
  private String licTypeName;
  private String number;
  private Instant validDate;
  private Instant expirationDate;
  private FileReference fileReference;
  private boolean snoozed;

  public String getId() {
    return String.format("%d-%d", getNurseId(), getLicTypeId());
  }

  public License setId(String id) {
    return this;
  }

  public long getNurseId() {
    return nurseId;
  }

  public License setNurseId(long nurseId) {
    this.nurseId = nurseId;
    return this;
  }

  public String getNurseName() {
    return nurseName;
  }

  public License setNurseName(String nurseName) {
    this.nurseName = nurseName;
    return this;
  }

  public Employee.Status getNurseStatus() {
    return nurseStatus;
  }

  public License setNurseStatus(Employee.Status nurseStatus) {
    this.nurseStatus = nurseStatus;
    return this;
  }

  public long getLicTypeId() {
    return licTypeId;
  }

  public License setLicTypeId(long licTypeId) {
    this.licTypeId = licTypeId;
    return this;
  }

  public String getLicTypeName() {
    return licTypeName;
  }

  public License setLicTypeName(String licTypeName) {
    this.licTypeName = licTypeName;
    return this;
  }

  public String getNumber() {
    return number;
  }

  public License setNumber(String number) {
    this.number = number;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getValidDate() {
    return validDate;
  }

  public License setValidDate(Instant validDate) {
    this.validDate = validDate;
    return this;
  }

  @ApiResourceProperty
  public Long validDate() {
    return validDate == null ? null : validDate.toEpochMilli();
  }

  public License setValidDate(long validDate) {
    this.validDate = Instant.ofEpochMilli(validDate);
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getExpirationDate() {
    return expirationDate;
  }

  public License setExpirationDate(Instant expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  @ApiResourceProperty
  public Long expirationDate() {
    return expirationDate == null ? null : expirationDate.toEpochMilli();
  }

  public License setExpirationDate(long expirationDate) {
    this.expirationDate = Instant.ofEpochMilli(expirationDate);
    return this;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public License setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
    return this;
  }

  public boolean isSnoozed() {
    return snoozed;
  }

  public License setSnoozed(boolean snoozed) {
    this.snoozed = snoozed;
    return this;
  }
}
