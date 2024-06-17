package com.digitald4.iis.model;

import static com.google.common.truth.Truth.assertThat;
import static java.time.Instant.ofEpochMilli;

import java.time.Instant;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

public class AppointmentTest {

  @Test
  public void setStart() {
    Instant start = ofEpochMilli(DateTime.parse("2024-05-01T12:23:37.000-07:00").getMillis());
    Appointment appointment = new Appointment();
    appointment.setStart(start);

    assertThat(appointment.start()).isEqualTo(start.toEpochMilli());
    assertThat(appointment.getDate())
        .isEqualTo(ofEpochMilli(DateTime.parse("2024-05-01T00:00:00.000-07:00").getMillis()));
    assertThat(appointment.getStartTime()).isEqualTo(ofEpochMilli(DateTime.parse("1970-01-01T12:23:37Z").getMillis()));
  }

  @Test
  public void setStart_utc() {
    Instant start = ofEpochMilli(DateTime.parse("2024-06-03T07:00:00Z").getMillis());
    Appointment appointment = new Appointment();
    appointment.setStart(start);

    assertThat(appointment.start()).isEqualTo(start.toEpochMilli());
    assertThat(appointment.getDate())
        .isEqualTo(ofEpochMilli(DateTime.parse("2024-06-03T00:00:00.000-07:00").getMillis()));
    assertThat(appointment.getStartTime()).isEqualTo(ofEpochMilli(DateTime.parse("1970-01-01T00:00:00Z").getMillis()));
  }
}
