package com.digitald4.iis.model;

import com.digitald4.common.model.ModelObjectModUser;
import com.digitald4.common.util.JSONUtil;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;

public class ServiceCode extends ModelObjectModUser<String> {
  private String code;
  public enum Type {Bill, Pay, Deprecated}
  private Type type;
  private Long vendorId;
  private String vendorName;
  private Double unitPrice;
  public enum Unit {Hour, Visit, Mile}
  private Unit unit;
  private String description;
  private boolean active = true;

  @Override
  public String getId() {
    return (vendorId == null ? "" : vendorId + "-") + getCode();
  }

  @Override
  public ServiceCode setId(String id) {
    super.setId(id);
    return this;
  }

  public String getCode() {
    return code;
  }

  public ServiceCode setCode(String code) {
    this.code = code;
    return this;
  }

  public Type getType() {
    return type;
  }

  public ServiceCode setType(Type type) {
    this.type = type;
    return this;
  }

  public Long getVendorId() {
    return vendorId;
  }

  public ServiceCode setVendorId(Long vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  public String getVendorName() {
    return vendorName;
  }

  public ServiceCode setVendorName(String vendorName) {
    this.vendorName = vendorName;
    return this;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public ServiceCode setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public Unit getUnit() {
    return unit;
  }

  public ServiceCode setUnit(Unit unit) {
    this.unit = unit;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ServiceCode setDescription(String description) {
    this.description = description;
    return this;
  }

  public boolean getActive() {
    return active;
  }

  public ServiceCode setActive(boolean active) {
    this.active = active;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  @Deprecated
  public String getToString() {
    return null;
  }

  @Override
  @ApiResourceProperty
  public String toString() {
    return vendorName == null ? description : String.format("%s - %s - %s", code, vendorName, unit);
  }

  public ServiceCode copy() {
    return JSONUtil.toObject(ServiceCode.class, JSONUtil.toJSON(this));
  }

  // For frontend to display service code for nurses, we make a copy then set these values.
  private Long nurseId;
  private String nurseName;

  @ApiResourceProperty
  public Long nurseId() {
    return nurseId;
  }

  @ApiResourceProperty
  public String nurseName() {
    return nurseName;
  }

  public ServiceCode setNurseInfo(Long nurseId, String nurseName) {
    this.nurseId = nurseId;
    this.nurseName = nurseName;
    return this;
  }
}
