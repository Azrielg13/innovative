package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;

public class License extends IP360Entity {
  private long nurseId;
  private String nurseName;
  private long licTypeId;
  private String licTypeName;
  private String number;
  private Long validDate;
  private Long expirationDate;
  private FileReference fileReference;

  public License setId(Long id) {
    super.setId(id);
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

  public Long getValidDate() {
    return validDate;
  }

  public License setValidDate(Long validDate) {
    this.validDate = validDate;
    return this;
  }

  public Long getExpirationDate() {
    return expirationDate;
  }

  public License setExpirationDate(Long expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public License setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
    return this;
  }
}
