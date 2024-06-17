package com.digitald4.iis.model;

import com.digitald4.common.model.Searchable;

public interface Employee extends Searchable {
  enum Status {Applicant, Rejected, Pending, Active, Hold, Suspended, Terminated}
  Long getId();
}
