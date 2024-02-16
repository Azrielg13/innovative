package com.digitald4.iis.model;

import com.digitald4.common.model.GenericHasModificationUser;
import com.google.api.server.spi.config.ApiResourceProperty;

public class Note extends GenericHasModificationUser<Long> {
  private String entityType;
  private String entityId;
  private StringBuilder note;
  enum Type {General, Important, Concerning}
  private Type type = Type.General;
  enum Status {Active, Archived}
  private Status status = Status.Active;

  private String creationUser;

  public String getEntityType() {
    return entityType;
  }

  public Note setEntityType(String entityType) {
    this.entityType = entityType;
    return this;
  }

  public String getEntityId() {
    return entityId;
  }

  public Note setEntityId(String entityId) {
    this.entityId = entityId;
    return this;
  }

  public StringBuilder getNote() {
    return note;
  }

  public Note setNote(StringBuilder note) {
    this.note = note;
    return this;
  }

  public Type getType() {
    return type;
  }

  public Note setType(Type type) {
    this.type = type;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Note setStatus(Status status) {
    this.status = status;
    return this;
  }

  @ApiResourceProperty
  public String creationUser() {
    return creationUser;
  }

  public Note setCreationUser(String creationUser) {
   this.creationUser = creationUser;
   return this;
  }
}
