package com.digitald4.iis.server;

import static com.google.common.truth.Truth.assertThat;

import com.digitald4.iis.model.Patient;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class PatientServiceTest {

  @Test
  public void testListForReport() {
    Iterable<Patient> results = new PatientService(null, null).transformForReport(ImmutableList.of(
        new Patient().setId(8575L).setFirstName("First").setLastName("Last").setBillingVendorId(1234L).setBillingVendorName("Billing Vendor")
    ));

    Patient result = results.iterator().next();
    assertThat(result.getId()).isEqualTo(8575L);
    assertThat(result.getFirstName()).isNull();
    assertThat(result.getLastName()).isNull();
    assertThat(result.getBillingVendorId()).isEqualTo(1234L);
    assertThat(result.getBillingVendorName()).isEqualTo("Billing Vendor");
  }
}
