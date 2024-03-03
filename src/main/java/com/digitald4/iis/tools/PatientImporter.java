package com.digitald4.iis.tools;

import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Patient.Gender;
import com.digitald4.iis.model.Patient.VisitFrequency;
import com.digitald4.iis.storage.GenData;
import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import org.json.JSONObject;

public class PatientImporter {
  private ImmutableList<String> columnNames;

  public PatientImporter setColumnNames(String line) {
    columnNames = Calculate.splitCSV(line);
    return this;
  }

  public ImmutableList<Patient> process(String filePath) {
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<Patient> patients = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        patients.add(parsePatient(line));
      }
      return patients.build();
    } catch (IOException | ArrayIndexOutOfBoundsException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  public Patient parsePatient(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Groups", "Tags V2", "Ssn", "Communication Method", "Services  Erl");
    String primaryPhone = json.optString("Phone Main", null);
    if (primaryPhone == null) {
      primaryPhone = json.optString("Client Registration Number", null);
    }
    return new Patient()
        .setId(json.optLong("Staffr Id"))
        .setGuId(json.optLong("Staffr Guid"))
        .setName(json.optString("First Name")+ " " + json.optString("Last Name"))
        .setMrNum(json.optString("Medicare Id", null))
        .setPhonePrimary(parsePhone(primaryPhone))
        .setPhoneAlternate(parsePhone(json.optString("Phone Other", null)))
        .setPhonePersonal(parsePhone(json.optString("Phone Personal", null)))
        .setServiceAddress(parseAddress(
            json.optString("Address"), json.optString("City"), json.optString("State"),
            json.optString("Zip"), json.optString("Address Suite", null)))
        .setEmail(json.optString("Email", null))
        .setReferralSourceName(json.optString("Referred By", null))
        .setGender(parseGender(json.optString("Gender", null)))
        .setDiagnosis(json.optString("Diagnosis", null))
        .setDateOfBirth(parseDate(json.optString("Date Of Birth", null)))
        .setRx(json.optString("Patient Rx", null))
        .setTherapyType(json.optString("Therapy Type", null))
        .setTherapyTypeId(parseTherapyType(json.optString("Therapy Type", null)))
        .setIvAccess(json.optString("Iv Access", null))
        .setIvAccessId(parseIvAccess(json.optString("Iv Access", null)))
        .setIvType(json.optString("Pump Or Gravity", null))
        .setIvPumpBrand(json.optString("Pump Brand", null))
        .setLabs(parseLabs(json.optString("Labs Yes Or No", null)))
        .setLabsFrequency(parseLabsFreq(json.optString("Labs Yes Or No", null)))
        .setVisitType(json.optString("Type Of Visit Soc Or Follow Up Or Recert", null))
        .setVisitTypeId(parseVisitTypeId(json.optString("Type Of Visit Soc Or Follow Up Or Recert", null)))
        // .setFirstRecertDue(json.optString("Recert Period From And To"))
        .setSchedulingPreference(json.optString("Scheduling Preference", null))
        .setVisitFrequency(parseVisitFreq(json.optString("One Time Visit Or Manage", null)))
        .setReferralNote(json.optString("Notes", null));
  }

  public static String parseString(String text) {
    return text.isEmpty() ? null : text;
  }

  public static Phone parsePhone(String text) {
    if (text == null) {
      return null;
    }

    Phone phone = new Phone();
    if (text.contains(" ")) {
      String type = text.substring(0, text.indexOf(" "));
      text = text.substring(text.indexOf(" ") + 1);
      switch (type) {
        case "Home": phone.setTypeId(GenData.PHONE_TYPE_HOME); break;
        case "Mobile": phone.setTypeId(GenData.PHONE_TYPE_MOBILE); break;
        case "Work": phone.setTypeId(GenData.PHONE_TYPE_WORK); break;
      }
    }
    return phone.setNumber(text);
  }

  public static Address parseAddress(String address, String city, String state, String zip, String unit) {
    return new Address()
        .setAddress(String.format("%s %s, %s, %s", address, city, state, zip))
        .setUnit(unit);
  }

  private static Gender parseGender(String text) {
    if (text != null) {
      switch (text) {
        case "M": return Gender.Male;
        case "F": return Gender.Female;
      }
    }
    return null;
  }

  public static Instant parseDate(String value) throws ParseException {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return Instant.ofEpochMilli(FormatText.parseDate(value).getTime());
  }

  private static Long parseDiagnosis(String text) {
    if (text == null) {
      return null;
    }

    return 1L;
  }

  private static Long parseTherapyType(String text) {
    return null;
  }

  private static Long parseIvAccess(String text) throws ParseException {
    if (text == null) {
      return null;
    }

    String value = text.contains(" ") ? text.substring(0, text.indexOf(' ')) : text;

    switch (value.toUpperCase()) {
      case "PICC": return GenData.IV_ACCESS_PICC;
      case "PIV":
      case "IV":
      case "PERIPHERAL":
        return GenData.IV_ACCESS_PERIPHERAL_IV;
      case "PORT": return GenData.IV_ACCESS_PORT;
      case "HICKMAN": return GenData.IV_ACCESS_HICKMAN;
      case "MIDLINE": return GenData.IV_ACCESS_MIDLINE;
    }

    if (text.contains("access port")) {
      return GenData.IV_ACCESS_PORT;
    }

    return null;
  }

  private static Long parseVisitTypeId(String text) {
    return null;
  }

  private static boolean parseLabs(String text) {
    return !(text == null || text.startsWith("n"));
  }

  private static String parseLabsFreq(String text) {
    return (text == null || text.length() < 4) ? null : text;
  }

  private static VisitFrequency parseVisitFreq(String text) {
    if (text != null) {
      text = text.toLowerCase();

      if (text.contains("manage")) {
        return VisitFrequency.MANAGED;
      } else if (text.contains("1x")) {
        return VisitFrequency.ONE_TIME;
      }
    }
    return null;
  }

  public static void assertEmpty(JSONObject json, String... colNames) {
    stream(colNames).filter(json::has)
        .filter(colName -> !(json.get(colName).equals(" - ") || json.getString(colName).equals("0") || json.getString(colName).equals("N/A")))
        .forEach(colName -> System.out.printf("Found value '%s' in column: %s\n", json.get(colName), colName));
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<Patient> patients = new PatientImporter().process("data/client-list.csv");
    patients.forEach(System.out::println);
    System.out.printf("Total size: %d\n", patients.size());
    /* patients.stream()
        .peek(p -> System.out.printf("Creating patient: %s %s\n", p.getId(), p.getName()))
        .forEach(dao::create);*/
  }
}
