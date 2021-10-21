package com.digitald4.iis.model;

public class Notification {
  public enum Type {INFO, WARNING, ERROR};

  public enum EntityType {ALL, NURSE, PATIENT, VENDOR};

  private final Type type;
  private final String title;
  private final long date;
  private final EntityType entityType;
  private final long entityId;

  public Notification(Type type, String title, long date, EntityType entityType, long entityId) {
    this.type = type;
    this.title = title;
    this.date = date;
    this.entityType = entityType;
    this.entityId = entityId;
  }

  public Type getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public long getDate() {
    return date;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public long getEntityId() {
    return entityId;
  }
}
