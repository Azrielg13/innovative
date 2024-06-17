package com.digitald4.iis.tools;

import static com.google.common.truth.Truth.assertThat;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.iis.model.Patient;
import com.google.common.collect.ImmutableListMultimap;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.text.ParseException;
import java.time.Instant;

public class PatientImporterTest {
  private final DataImporter<Patient> patientImporter = new PatientImporter(ImmutableListMultimap.of()).setColumnNames(
      ",Internal Id,Full Name,Status,Email,Phone (Main),Phone (Other),Phone (Personal),Address,Address Line 2,City,"
      + "State,Zip Code,Diagnosis,Patient RX,Date of Birth,Medicare ID,One time visit or Manage,Referred By,Pump Brand,"
      + "Pump or Gravity,Scheduling Preference,Therapy Type,Type of Visit SOC or Follow up or Recert,"
      + "Recert Period From and To,Social Security Number,Tags,Peds or Adult,Labs Yes or No,IV Access,Gender,"
      + "First Name,Last Name,Notes,Client Registration Number,Communication Method");

  @Test
  public void parsePatient_minimumData() throws ParseException {
    String line =
        "1,1925, Chuck  Badman,discharged,,724-627-7332,,,32291 Farwell Dr ,,Garden Grove,CA,92845,,Prolastin for 3 visits only ,1946-01-19,,,Tim/Sal,,,,,,,,,Adult,,,M, Chuck ,Badman,,,";
    assertThat(patientImporter.parse(line)).isEqualTo(
        new Patient().setId(1925L).setFirstName("Chuck").setLastName("Badman").setStatus(Patient.Status.Discharged)
            .setGender(Patient.Gender.Male)
            .setPhonePrimary(new Phone().setNumber("724-627-7332"))
            .setServiceAddress(new Address().setAddress("32291 Farwell Dr Garden Grove, CA, 92845"))
            .setRx("Prolastin for 3 visits only")
            .setReferralSource("Tim/Sal")
            .setDateOfBirth(Instant.parse("1946-01-19T08:00:00.00Z")));
  }

  @Test
  public void parsePatient_fullAddressInAddressField() throws ParseException {
    String line =
        "2,2927,ACD LLC COVID TESTING-PMH,discharged,,,,,\"2321 Pullman St, Santa Ana, CA 92705\",,,,,,,,,,,,,,,,,,,,,,,ACD LLC,COVID TESTING-PMH,,,";
    assertThat(patientImporter.parse(line)).isEqualTo(
        new Patient().setId(2927L).setFirstName("ACD LLC").setLastName("COVID TESTING-PMH").setStatus(Patient.Status.Discharged)
            .setServiceAddress(new Address().setAddress("2321 Pullman St, Santa Ana, CA 92705")));
  }

  @Test
  public void parsePatient_withCommaInData() throws ParseException {
    String line =
        "2070,5828,Bryan Shem Abraham,discharged,,959-432-1146,,,1244 Wolfrun Dr,,Chino Hills,CA,91709,\"E84.9 Cystic fibrosis, unspecified\",Ceftazidime 3gm/100mL NS IV EVERY 6 HOURS - Easypump Teaching w/ labs 9/6 & 9/13,2012-12-31,,,COR-LA,,,,,,,,,Adult,,,,Bryan Shem,Abraham,,,";
    assertThat(patientImporter.parse(line)).isEqualTo(
        new Patient().setId(5828L).setFirstName("Bryan Shem").setLastName("Abraham").setStatus(Patient.Status.Discharged)
            .setPhonePrimary(new Phone().setNumber("959-432-1146"))
            .setServiceAddress(new Address().setAddress("1244 Wolfrun Dr Chino Hills, CA, 91709"))
            .setReferralSource("COR-LA")
            .setDiagnosis("E84.9 Cystic fibrosis, unspecified")
            .setDateOfBirth(Instant.parse("2012-12-31T08:00:00.00Z"))
            .setRx("Ceftazidime 3gm/100mL NS IV EVERY 6 HOURS - Easypump Teaching w/ labs 9/6 & 9/13")
    );
  }
}
