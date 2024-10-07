package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;
import com.digitald4.common.model.ModelObjectModUser;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import java.time.Instant;

public class QuickBooksExport extends ModelObjectModUser<String> {
  private ImmutableList<Long> appointmentIds;
  private FileReference fileReference;
  private FileReference invoiceFileReference;
  private Instant billingDate;
  private String comment;

  public QuickBooksExport setId(String id) {
    super.setId(id);
    return this;
  }

  public ImmutableList<Long> getAppointmentIds() {
    return appointmentIds;
  }

  public QuickBooksExport setAppointmentIds(Iterable<Long> appointmentIds) {
    this.appointmentIds = ImmutableList.copyOf(appointmentIds);
    return this;
  }

  public FileReference getFileReference() {
    return fileReference;
  }

  public QuickBooksExport setFileReference(FileReference fileReference) {
    this.fileReference = fileReference;
    return this;
  }

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getBillingDate() {
    return billingDate != null ? billingDate : getCreationTime();
  }

  public QuickBooksExport setBillingDate(Instant billingDate) {
    this.billingDate = billingDate;
    return this;
  }

  @ApiResourceProperty
  public Long billingDate() {
    return getBillingDate() == null ? null : getBillingDate().toEpochMilli();
  }

  public QuickBooksExport setBillingDate(long date) {
    this.billingDate = Instant.ofEpochMilli(date);
    return this;
  }

  public String getComment() {
    return comment;
  }

  public QuickBooksExport setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public FileReference getInvoiceFileReference() {
    return invoiceFileReference;
  }

  public QuickBooksExport setInvoiceFileReference(FileReference invoiceFileReference) {
    this.invoiceFileReference = invoiceFileReference;
    return this;
  }
}
