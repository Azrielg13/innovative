package com.digitald4.iis.model;

public interface Employee {
  enum Status {Applicant, Rejected, Pending, Active, Hold, Suspended, Terminated}
  public Long getId();
}
