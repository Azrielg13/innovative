package com.digitald4.iis.model;

import com.digitald4.common.util.JSONUtil;
import org.junit.Test;

public class PatientTest {

  @Test
  public void loadFromJson() {
    String json = "{\"billingVendorId\":0,\"billingRate\":0,\"rx\":\"Rimdes\",\"dianosisId\":842,\"emergencyContactPhone\":{\"number\":\"213-310-6060\",\"typeId\":852},\"therapyTypeId\":852,\"labsFrequency\":\"Weekly\",\"firstRecertDue\":1636358400000,\"patientStatusId\":1649,\"mrNum\":\"md num\",\"infoInSOS\":true,\"referralDate\":1635058800000,\"dcDate\":0,\"schedulingPreference\":\"M,W,F\",\"id\":6209193342140416,\"startOfCareDate\":0,\"medsDeliveryDate\":0,\"patientConfirmationDate\":0,\"mileageRate\":0,\"nurseConfirmationDate\":0,\"billingRate2HrSoc\":0,\"emergencyContact\":\"Nipsy Hustle\",\"billingRate2HrRoc\":0,\"vendorConfirmationDate\":0,\"active\":false,\"serviceAddress\":{\"address\":\"1049 W 45th St, Los Angeles, CA 90037, USA\",\"latitude\":34.0028829,\"longitude\":-118.2932923},\"dateOfBirth\":0,\"referralNote\":\"This is the first test, lets see how far we get.\",\"billingFlat\":0,\"estLastDayOfService\":1637308800000,\"primaryPhone\":{\"number\":\"310-456-8923\"},\"ivAccessId\":0,\"labs\":false,\"billingFlat2HrRoc\":0,\"billingFlat2HrSoc\":0,\"name\":\"Patient One\",\"referralResolutionDate\":0,\"medsConfirmationDate\":0,\"referralResolutionId\":0,\"referralSourceId\":0}";

    Patient patient = JSONUtil.toObject(Patient.class, json);
  }
}
