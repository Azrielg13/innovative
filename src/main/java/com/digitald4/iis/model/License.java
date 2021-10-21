package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;

public class License {
  private long id;
  private long nurseId;
  private String nurseName;
  private long licTypeId;
  private String licTypeName;
  private String number;
  private long validDate;
  private long expirationDate;
  private FileReference fileReference;

  public long getId() {
    return id;
  }

  public License setId(long id) {
    this.id = id;
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

  public long getValidDate() {
    return validDate;
  }

  public License setValidDate(long validDate) {
    this.validDate = validDate;
    return this;
  }

  public long getExpirationDate() {
    return expirationDate;
  }

  public License setExpirationDate(long expirationDate) {
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
