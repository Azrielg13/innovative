package com.digitald4.iis.tools;

import static com.google.common.truth.Truth.assertThat;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.iis.model.Patient;
import java.text.ParseException;
import java.time.Instant;
import org.junit.Test;

public class PatientImporterTest {
  private final PatientImporter patientImporter = new PatientImporter().setColumnNames(
      "Staffr Id,Staffr Guid,Groups,First Name,Tags V2,Last Name,Ssn,Medicare Id,"
          + "Communication Method,Client Registration Number,Phone Main,Phone Other,Phone Personal,"
          + "Address,Address Suite,City,State,Zip,Email,Referred By,Gender,Peds Or Adult,Diagnosis,"
          + "Date Of Birth,Patient Rx,Therapy Type,Iv Access,Pump Or Gravity,Pump Brand,"
          + "Labs Yes Or No,Type Of Visit Soc Or Follow Up Or Recert,Recert Period From And To,"
          + "Scheduling Preference,One Time Visit Or Manage,Notes,Services  Erl");
  @Test
  public void parsePatient_minimumData() throws ParseException {
    String line =
        "1925,259105,,Chuck,,Badman,,,,,724-627-7332,,,5170 Edward War Square,,Garden Grove,CA,92845,,,,,,1/19/1958,,,,,,,,,,,,";
    assertThat(patientImporter.parsePatient(line)).isEqualTo(
        new Patient().setId(1925L).setGuId(259105L).setName("Chuck Badman")
            .setPhonePrimary(new Phone().setNumber("724-627-7332"))
            .setServiceAddress(
                new Address().setAddress("5170 Edward War Square Garden Grove, CA, 92845"))
            .setDateOfBirth(Instant.parse("1958-01-19T08:00:00.00Z")));
  }

  @Test
  public void parsePatient_minimumData2() throws ParseException {
    String line =
        "3028,310138,,Taylor,,Abman,,,,,301-294-9913,,,7542 Via Gross,Unit 131,Los Angeles,CA,90042,,,,,,10/27/1992,,,,,,,,,,,,";
    assertThat(patientImporter.parsePatient(line)).isEqualTo(
        new Patient().setId(3028L).setGuId(310138L).setName("Taylor Abman")
            .setPhonePrimary(new Phone().setNumber("301-294-9913"))
            .setServiceAddress(new Address()
                .setAddress("7542 Via Gross Los Angeles, CA, 90042").setUnit("Unit 131"))
            .setDateOfBirth(Instant.parse("1992-10-27T08:00:00.00Z")));
  }

  @Test
  public void parsePatient_withCommaInData() throws ParseException {
    String line =
        "5828,476140,,Bryan Shem,,Abraham,,,,,959-432-1146,,,1244 Wolfrun Dr,,Chino Hills,CA,91709,,COR-LA,,Adult,\"E84.9 Cystic fibrosis, unspecified\",12/31/2012,Ceftazidime 3gm/100mL NS IV EVERY 6 HOURS - Easypump Teaching w/ labs 9/6 & 9/13,,,,,,,,,,,";
    assertThat(patientImporter.parsePatient(line)).isEqualTo(
        new Patient().setId(5828L).setGuId(476140L).setName("Bryan Shem Abraham")
            .setPhonePrimary(new Phone().setNumber("959-432-1146"))
            .setServiceAddress(new Address().setAddress("1244 Wolfrun Dr Chino Hills, CA, 91709"))
            .setReferralSourceName("COR-LA")
            .setDiagnosis("E84.9 Cystic fibrosis, unspecified")
            .setDateOfBirth(Instant.parse("2012-12-31T08:00:00.00Z"))
            .setRx("Ceftazidime 3gm/100mL NS IV EVERY 6 HOURS - Easypump Teaching w/ labs 9/6 & 9/13")
    );
  }
}
