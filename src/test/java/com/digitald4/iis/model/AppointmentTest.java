package com.digitald4.iis.model;

import static com.google.common.truth.Truth.assertThat;
import static java.time.Instant.ofEpochMilli;

import com.digitald4.common.util.JSONUtil;
import java.time.Instant;
import org.joda.time.DateTime;
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

  @Test
  public void loadJson() {
    Appointment appointment = JSONUtil.toObject(Appointment.class,
        "{\"date\":1745305200000,\"assessmentApproved\":false,\"nurseName\":\"Dr Greg Ross\",\"creationUsername\":\"eddiemay\",\"lastModifiedTime\":1745607364201,\"creationTime\":1745433123476,\"patientId\":6227693880213504,\"nurseId\":6217038301233152,\"vendorId\":6262417818386432,\"deletionTime\":1745607364004,\"repeat\":{\"type\":\"Does_not_repeat\"},\"startTime\":54000000,\"deletionUsername\":\"eddiemay\",\"id\":4909378276687872,\"state\":\"DELETED\",\"mileage\":0,\"patientName\":\"Decan St John\",\"assessments\":[],\"assessmentComplete\":false,\"start\":1745305200000,\"vendorName\":\"Doctors Hospital\",\"loggedHours\":0,\"cancelled\":false,\"endTime\":61200000,\"lastModifiedUsername\":\"eddiemay\"}");

    assertThat(appointment.getDate()).isEqualTo(Instant.ofEpochMilli(1745305200000L));
    assertThat(appointment.getNurseName()).isEqualTo("Dr Greg Ross");
    assertThat(appointment.getDeletionTime()).isEqualTo(Instant.ofEpochMilli(1745607364004L));
  }
}
