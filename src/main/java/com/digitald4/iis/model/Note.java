package com.digitald4.iis.model;

import com.digitald4.common.model.GenericHasModificationUser;

public class Note extends GenericHasModificationUser<Long> {
  private String entityType;
  private String entityId;
  private String entityName;
  private StringBuilder note;
  public enum Type {General, Important, Concerning, Terminated}
  private Type type = Type.General;
  public enum Status {Active, Archived}
  private Status status = Status.Active;
  private String creationUsername;

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

  public String getEntityName() {
    return entityName;
  }

  public Note setEntityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

  public StringBuilder getNote() {
    return note;
  }

  public Note setNote(StringBuilder note) {
    this.note = note;
    return this;
  }

  public Note setNote(String note) {
    return setNote(new StringBuilder(note));
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

  @Deprecated
  public String getCreationUserName() {
    return null;
  }

  @Deprecated
  public Note setCreationUserName(String creationUserName) {
   this.creationUsername = creationUserName;
   return this;
  }

  public String getCreationUsername() {
    return creationUsername;
  }

  public Note setCreationUsername(String creationUsername) {
    this.creationUsername = creationUsername;
    return this;
  }
}
