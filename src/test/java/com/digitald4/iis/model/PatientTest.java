package com.digitald4.iis.model;

import static com.google.common.truth.Truth.assertThat;

import com.digitald4.common.util.JSONUtil;
import org.junit.Test;

public class PatientTest {

  @Test
  public void loadFromJson() {
    String json = "{\"billingVendorId\":0,\"billingRate\":0,\"rx\":\"Rimdes\",\"diagnosis\":\"sick\",\"emergencyContactPhone\":{\"number\":\"213-310-6060\",\"typeId\":852},\"therapyTypeId\":852,\"labsFrequency\":\"Weekly\",\"firstRecertDue\":1636358400000,\"patientStatusId\":1649,\"mrNum\":\"md num\",\"infoInSOS\":true,\"referralDate\":1635058800000,\"dcDate\":0,\"schedulingPreference\":\"M,W,F\",\"id\":6209193342140416,\"startOfCareDate\":0,\"medsDeliveryDate\":0,\"patientConfirmationDate\":0,\"mileageRate\":0,\"nurseConfirmationDate\":0,\"billingRate2HrSoc\":0,\"emergencyContact\":\"Nipsy Hustle\",\"billingRate2HrRoc\":0,\"vendorConfirmationDate\":0,\"active\":false,\"serviceAddress\":{\"address\":\"1049 W 45th St, Los Angeles, CA 90037, USA\",\"latitude\":34.0028829,\"longitude\":-118.2932923},\"dateOfBirth\":0,\"referralNote\":\"This is the first test, lets see how far we get.\",\"billingFlat\":0,\"estLastDayOfService\":1637308800000,\"primaryPhone\":{\"number\":\"310-456-8923\"},\"ivAccessId\":0,\"labs\":false,\"billingFlat2HrRoc\":0,\"billingFlat2HrSoc\":0,\"name\":\"Patient One\",\"referralResolutionDate\":0,\"medsConfirmationDate\":0,\"referralResolutionId\":0,\"referralSourceId\":0}";

    Patient patient = JSONUtil.toObject(Patient.class, json);
    assertThat(patient.getRx()).isEqualTo("Rimdes");
    assertThat(patient.getDiagnosis()).isEqualTo("sick");
  }

  @Test
  public void setName() {
    assertThat(new Patient().setName("First Last"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Last").setName("First Last"));
    assertThat(new Patient().setName("First Last "))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Last").setName("First Last"));
    assertThat(new Patient().setName(" First Last"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Last").setName("First Last"));
    assertThat(new Patient().setName("First Last Peds"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Last Peds").setName("First Last Peds"));
    assertThat(new Patient().setName("First Last-PEDS"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Last-PEDS").setName("First Last-PEDS"));
    assertThat(new Patient().setName("First Hypen-Last Peds"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Hypen-Last Peds").setName("First Hypen-Last Peds"));
    assertThat(new Patient().setName("First Hypen-Last Jr"))
        .isEqualTo(new Patient().setFirstName("First").setLastName("Hypen-Last Jr").setName("First Hypen-Last Jr"));
    assertThat(new Patient().setName("First Hypen-Last Peds [s]")).isEqualTo(
        new Patient().setFirstName("First").setLastName("Hypen-Last Peds [s]").setName("First Hypen-Last Peds [s]"));
  }
}
