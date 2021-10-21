package com.digitald4.iis.model;

import com.google.api.server.spi.config.ApiResourceProperty;

public class User implements com.digitald4.common.model.User {
  private long id;
  private int typeId;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private boolean disabled;
  private boolean readOnly;
  private String notes;

  @Override
  public long getId() {
    return id;
  }

  @Override
  public User setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public int getTypeId() {
    return typeId;
  }

  @Override
  public User setTypeId(int typeId) {
    this.typeId = typeId;
    return this;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  @ApiResourceProperty
  public String fullName() {
    return String.format("%s %s", getFirstName(), getLastName());
  }

  public boolean isDisabled() {
    return disabled;
  }

  public User setDisabled(boolean disabled) {
    this.disabled = disabled;
    return this;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public User setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

  public String getNotes() {
    return notes;
  }

  public User setNotes(String notes) {
    this.notes = notes;
    return this;
  }
}
