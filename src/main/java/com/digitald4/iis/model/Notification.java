package com.digitald4.iis.model;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.time.Instant;

public class Notification {
  public enum Type {INFO, WARNING, ERROR};

  public enum EntityType {ALL, NURSE, PATIENT, VENDOR};

  private final Type type;
  private final String title;
  private final Instant date;
  private final EntityType entityType;
  private final long entityId;

  public Notification(Type type, String title, Instant date, EntityType entityType, long entityId) {
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

  @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
  public Instant getDate() {
    return date;
  }

  @ApiResourceProperty
  public long date() {return date.toEpochMilli();}

  public EntityType getEntityType() {
    return entityType;
  }

  public long getEntityId() {
    return entityId;
  }
}
