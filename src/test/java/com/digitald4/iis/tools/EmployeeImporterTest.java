package com.digitald4.iis.tools;

import static com.google.common.truth.Truth.assertThat;

import com.digitald4.iis.model.Nurse;
import org.junit.Test;

public class EmployeeImporterTest {
  @Test
  public void parsePayRate() {
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "10/20"))
        .isEqualTo(new Nurse().setPayRate(10.0).setPayFlat(20.0));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "22.22/40"))
        .isEqualTo(new Nurse().setPayRate(22.22).setPayFlat(40.0));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "35.30/65.50"))
        .isEqualTo(new Nurse().setPayRate(35.30).setPayFlat(65.5));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "35.30/75/.55"))
        .isEqualTo(new Nurse().setPayRate(35.30).setPayFlat(75.0).setMileageRate(.55));

    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "$43.21/82.50"))
        .isEqualTo(new Nurse().setPayRate(43.21).setPayFlat(82.5));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "$53.21/$106.50"))
        .isEqualTo(new Nurse().setPayRate(53.21).setPayFlat(106.5));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "63.21 per hr/128.50"))
        .isEqualTo(new Nurse().setPayRate(63.21).setPayFlat(128.5));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "74.21 per hr/144.50 per visit"))
        .isEqualTo(new Nurse().setPayRate(74.21).setPayFlat(144.5));
    assertThat(EmployeeImporter.parsePayRate(new Nurse(), "$83.21 per hr/$160.50"))
        .isEqualTo(new Nurse().setPayRate(83.21).setPayFlat(160.5));
    /* assertThat(EmployeeImporter.parsePayRate(new Nurse(), "55/100 as of 09/07/18"))
        .isEqualTo(new Nurse().setPayRate(55.0).setPayFlat(110.0)); */
  }
}
