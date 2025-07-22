package com.digitald4.iis.model;

import com.digitald4.common.model.ChangeTrackable;
import com.digitald4.common.model.ModelObject;

public class ReferralResponse extends ModelObject<String> implements ChangeTrackable<String> {
  private long patientId;
  private long nurseId;
  public enum Response {Interested, Not_Interested, Not_Qualified}
  private Response response;
  private String comment;

  @Override
  public String getId() {
    return String.format("%d-%d", patientId, nurseId);
  }

  public long getPatientId() {
    return patientId;
  }

  public ReferralResponse setPatientId(long patientId) {
    this.patientId = patientId;
    return this;
  }

  public long getNurseId() {
    return nurseId;
  }

  public ReferralResponse setNurseId(long nurseId) {
    this.nurseId = nurseId;
    return this;
  }

  public Response getResponse() {
    return response;
  }

  public ReferralResponse setResponse(Response response) {
    this.response = response;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public ReferralResponse setComment(String comment) {
    this.comment = comment;
    return this;
  }
}
