package com.digitald4.iis.model;

import com.digitald4.common.model.FileReference;
import com.digitald4.common.model.ModelObjectModUser;
import com.google.common.collect.ImmutableList;

public class QuickBooksExport extends ModelObjectModUser<String> {
  private ImmutableList<Long> appointmentIds;
  private FileReference fileReference;
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

  public String getComment() {
    return comment;
  }

  public QuickBooksExport setComment(String comment) {
    this.comment = comment;
    return this;
  }
}
