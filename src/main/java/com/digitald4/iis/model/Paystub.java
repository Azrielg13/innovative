package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import java.time.Instant;

public class Paystub extends IP360Entity {
  public enum DeductionType {PRETAX, TAX, POSTTAX};

  private long nurseId;
  private long statusId;
  private ImmutableList<Long> appointmentIds;
  private String nurseName;
  private Instant payDate;
  private String comment;
  private double loggedHours;
  private double grossPay;
  private double mileage;
  private double payMileage;
  private ImmutableList<Deduction> deductions = ImmutableList.of();
  private double preTaxDeduction;
  private double taxable;
  private double taxTotal;
  private double postTaxDeduction;
  private double nonTaxWages;
  private double netPay;
  private Instant generationTime;
  private double loggedHoursYTD;
  private double grossPayYTD;
  private double mileageYTD;
  private double payMileageYTD;
  private double preTaxDeductionYTD;
  private double taxableYTD;
  private double taxTotalYTD;
  private double postTaxDeductionYTD;
  private double nonTaxWagesYTD;
  private double netPayYTD;
  private FileReference fileReference;

  public Paystub setId(Long id) {
    super.setId(id);
    return this;
  }

  public long getNurseId() {
    return nurseId;
  }

  public Paystub setNurseId(long nurseId) {
    this.nurseId = nurseId;
    return this;
  }

  public long getStatusId() {
    return statusId;
  }

  public Paystub setStatusId(long statusId) {
    this.statusId = statusId;
    return this;
  }

  public ImmutableList<Long> getAppointmentIds() {
    return appointmentIds;
  }

  public Paystub setAppointmentIds(Iterable<Long> appointmentIds) {
    this.appointmentIds = ImmutableList.copyOf(appointmentIds);
    return this;
  }

  public String getNurseName() {
    return nurseName;
  }

  public Paystub setNurseName(String nurseName) {
    this.nurseName = nurseName;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getPayDate() {
    return payDate;
  }

  public Paystub setPayDate(Instant payDate) {
    this.payDate = payDate;
    return this;
  }

  @ApiResourceProperty
  public long payDate() {
    return payDate.toEpochMilli();
  }

  public Paystub setPayDate(long payDate) {
    this.payDate = Instant.ofEpochMilli(payDate);
    return this;
  }

  public String getComment() {
    return comment;
  }

  public Paystub setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public double getLoggedHours() {
    return loggedHours;
  }

  public Paystub setLoggedHours(double loggedHours) {
    this.loggedHours = loggedHours;
    return this;
  }

  public double getGrossPay() {
    return grossPay;
  }

  public Paystub setGrossPay(double grossPay) {
    this.grossPay = grossPay;
    return this;
  }

  public double getMileage() {
    return mileage;
  }

  public Paystub setMileage(double mileage) {
    this.mileage = mileage;
    return this;
  }

  public double getPayMileage() {
    return payMileage;
  }

  public Paystub setPayMileage(double payMileage) {
    this.payMileage = payMileage;
    return this;
  }

  public ImmutableList<Deduction> getDeductions() {
    return deductions;
  }

  public Paystub setDeductions(Iterable<Deduction> deductions) {
    this.deductions = ImmutableList.copyOf(deductions);
    return this;
  }

  public double getPreTaxDeduction() {
    return preTaxDeduction;
  }

  public Paystub setPreTaxDeduction(double preTaxDeduction) {
    this.preTaxDeduction = preTaxDeduction;
    return this;
  }

  public double getTaxable() {
    return taxable;
  }

  public Paystub setTaxable(double taxable) {
    this.taxable = taxable;
    return this;
  }

  public double getTaxTotal() {
    return taxTotal;
  }

  public Paystub setTaxTotal(double taxTotal) {
    this.taxTotal = taxTotal;
    return this;
  }

  public double getPostTaxDeduction() {
    return postTaxDeduction;
  }

  public Paystub setPostTaxDeduction(double postTaxDeduction) {
    this.postTaxDeduction = postTaxDeduction;
    return this;
  }

  public double getNonTaxWages() {
    return nonTaxWages;
  }

  public Paystub setNonTaxWages(double nonTaxWages) {
    this.nonTaxWages = nonTaxWages;
    return this;
  }

  public double getNetPay() {
    return netPay;
  }

  public Paystub setNetPay(double netPay) {
    this.netPay = netPay;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getGenerationTime() {
    return generationTime;
  }

  public Paystub setGenerationTime(Instant generationTime) {
    this.generationTime = generationTime;
    return this;
  }

  @ApiResourceProperty
  public long generationTime() {
    return generationTime.toEpochMilli();
  }

  public Paystub setGenerationTime(long generationTime) {
    this.generationTime = Instant.ofEpochMilli(generationTime);
    return this;
  }

  public double getLoggedHoursYTD() {
    return loggedHoursYTD;
  }

  public Paystub setLoggedHoursYTD(double loggedHoursYTD) {
    this.loggedHoursYTD = loggedHoursYTD;
    return this;
  }

  public double getGrossPayYTD() {
    return grossPayYTD;
  }

  public Paystub setGrossPayYTD(double grossPayYTD) {
    this.grossPayYTD = grossPayYTD;
    return this;
  }

  public double getMileageYTD() {
    return mileageYTD;
  }

  public Paystub setMileageYTD(double mileageYTD) {
    this.mileageYTD = mileageYTD;
    return this;
  }

  public double getPayMileageYTD() {
    return payMileageYTD;
  }

  public Paystub setPayMileageYTD(double payMileageYTD) {
    this.payMileageYTD = payMileageYTD;
    return this;
  }

  public double getPreTaxDeductionYTD() {
    return preTaxDeductionYTD;
  }

  public Paystub setPreTaxDeductionYTD(double preTaxDeductionYTD) {
    this.preTaxDeductionYTD = preTaxDeductionYTD;
    return this;
  }

  public double getTaxableYTD() {
    return taxableYTD;
  }

  public Paystub setTaxableYTD(double taxableYTD) {
    this.taxableYTD = taxableYTD;
    return this;
  }

  public double getTaxTotalYTD() {
    return taxTotalYTD;
  }

  public Paystub setTaxTotalYTD(double taxTotalYTD) {
    this.taxTotalYTD = taxTotalYTD;
    return this;
  }

  public double getPostTaxDeductionYTD() {
    return postTaxDeductionYTD;
  }

  public Paystub setPostTaxDeductionYTD(double postTaxDeductionYTD) {
    this.postTaxDeductionYTD = postTaxDeductionYTD;
    return this;
  }

  public double getNonTaxWagesYTD() {
    return nonTaxWagesYTD;
  }

  public Paystub setNonTaxWagesYTD(double nonTaxWagesYTD) {
    this.nonTaxWagesYTD = nonTaxWagesYTD;
    return this;
  }

  public double getNetPayYTD() {
    return netPayYTD;
  }

  public Paystub setNetPayYTD(double netPayYTD) {
    this.netPayYTD = netPayYTD;
    return this;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public Paystub setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
    return this;
  }

  public static class Deduction {
    private long typeId;
    private DeductionType type;
    private double factor;
    private double amount;
    private double amountYTD;

    public long getTypeId() {
      return typeId;
    }

    public Deduction setTypeId(long typeId) {
      this.typeId = typeId;
      return this;
    }

    public DeductionType getType() {
      return type;
    }

    public Deduction setType(DeductionType type) {
      this.type = type;
      return this;
    }

    public double getFactor() {
      return factor;
    }

    public Deduction setFactor(double factor) {
      this.factor = factor;
      return this;
    }

    public double getAmount() {
      return amount;
    }

    public Deduction setAmount(double amount) {
      this.amount = amount;
      return this;
    }

    public double getAmountYTD() {
      return amountYTD;
    }

    public Deduction setAmountYTD(double amountYTD) {
      this.amountYTD = amountYTD;
      return this;
    }
  }
}
