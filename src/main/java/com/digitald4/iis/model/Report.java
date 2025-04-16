package com.digitald4.iis.model;

import com.digitald4.common.model.ModelObjectModUser;
import com.digitald4.iis.model.User.RoleAbb;
import com.google.common.collect.ImmutableList;

public class Report extends ModelObjectModUser<String> {
  private String title;
  private String shortName;
  private ImmutableList<RoleAbb> roles = ImmutableList.of(RoleAbb.ADMIN);

  @Deprecated
  public String getName() {
    return null;
  }

  @Deprecated
  public Report setName(String name) {
    this.title = name;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Report setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getShortName() {
    return shortName;
  }

  public Report setShortName(String shortName) {
    this.shortName = shortName;
    return this;
  }

  public ImmutableList<RoleAbb> getRoles() {
    return roles;
  }

  public Report setRoles(Iterable<RoleAbb> roles) {
    this.roles = ImmutableList.copyOf(roles);
    return this;
  }

  public boolean meetsCriteria(RoleAbb role) {
    return roles.contains(role);
  }
}
