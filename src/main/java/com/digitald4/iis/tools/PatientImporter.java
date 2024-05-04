package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.*;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Patient.Gender;
import com.digitald4.iis.model.Patient.VisitFrequency;
import com.digitald4.iis.storage.GenData;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatientImporter implements DataImporter<Patient> {
  private final ImmutableListMultimap<Long, Long> associations;
  private ImmutableList<String> columnNames;

  public PatientImporter(ImmutableListMultimap<Long, Long> associations) {
    this.associations = associations;
  }

  public PatientImporter() {
    this(getClientFunderAssociations());
  }

  @Override
  public PatientImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    return this;
  }

  @Override
  public ImmutableList<Patient> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Client - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<Patient> process(String filePath) {
    System.out.println("Importing file: " + filePath);
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<Patient> patients = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        patients.add(parse(line));
      }
      return patients.build();
    } catch (Exception e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  @Override
  public Patient parse(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Groups", "Tags V2", "Social Security Number", "Communication Method", "Services  Erl");
    String primaryPhone = json.optString("Phone (Main)", null);
    if (primaryPhone == null) {
      primaryPhone = json.optString("Client Registration Number", null);
    }
    long id = json.optLong("Internal Id");
    Long vendorId = associations.get(id).isEmpty() ? null : associations.get(id).get(0);
    return new Patient()
        .setId(id)
        .setName(json.optString("First Name")+ " " + json.optString("Last Name"))
        .setStatus(getStatus(json.getString("Status")))
        .setMrNum(json.optString("Medicare Id", null))
        .setPhonePrimary(parsePhone(primaryPhone))
        .setPhoneAlternate(parsePhone(json.optString("Phone (Other)", null)))
        .setPhonePersonal(parsePhone(json.optString("Phone (Personal)", null)))
        .setServiceAddress(parseAddress(json))
        .setEmail(json.optString("Email", null))
        .setReferralSourceName(json.optString("Referred By", null))
        .setBillingVendorId(vendorId)
        .setGender(parseGender(json.optString("Gender")))
        .setDiagnosis(json.optString("Diagnosis", null))
        .setDateOfBirth(parseDate(json.optString("Date of Birth", null)))
        .setRx(json.optString("Patient RX", null))
        .setPreferredLanguage(json.optString("Preferred Language", null))
        .setTherapyType(json.optString("Therapy Type", null))
        .setTherapyTypeId(parseTherapyType(json.optString("Therapy Type", null)))
        .setIvAccess(json.optString("IV Access", null))
        .setIvAccessId(parseIvAccess(json.optString("IV Access", null)))
        .setIvType(json.optString("Pump or Gravity", null))
        .setIvPumpBrand(json.optString("Pump Brand", null))
        .setLabs(parseLabs(json.optString("Labs Yes or No", null)))
        .setLabsFrequency(parseLabsFreq(json.optString("Labs Yes or No", null)))
        .setVisitType(json.optString("Type of Visit SOC or Follow up or Recert", null))
        .setVisitTypeId(parseVisitTypeId(json.optString("Type of Visit SOC or Follow up or Recert", null)))
        // .setFirstRecertDue(json.optString("Recert Period From and To"))
        .setSchedulingPreference(json.optString("Scheduling Preference", null))
        .setVisitFrequency(parseVisitFreq(json.optString("One Time Visit or Manage", null)))
        .setTimeZone(json.optString("Time Zone", null))
        .setReferralNote(json.optString("Notes", null));
  }

  public static ImmutableListMultimap<Long, Long> getClientFunderAssociations() {
    String filePath = stream(new File("data/").list()).filter(f -> f.startsWith("Client-Funder")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow();

    System.out.println("Importing file: " + filePath);
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableListMultimap.Builder<Long, Long> associations = ImmutableListMultimap.builder();
      var columnNames = Calculate.splitCSV(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        JSONObject json = Calculate.jsonFromCSV(columnNames, line);
        if (json.has("Internal Id")) {
          associations.put(json.getLong("Internal Id"), json.getLong("Id"));
        }
      }
      return associations.build();
    } catch (Exception e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line #" + lineNum + ": " + line, e);
    }
  }

  public static Patient.Status getStatus(String value) {
    return value.equals("on_hold")
        ? Patient.Status.On_Hold : Patient.Status.valueOf(value.substring(0, 1).toUpperCase() + value.substring(1));
  }

  private static Gender parseGender(String text) {
    return switch (text) {
      case "M" -> Gender.Male;
      case "F" -> Gender.Female;
      default -> null;
    };
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

    return switch (value.toUpperCase()) {
      case "PICC" -> GenData.IV_ACCESS_PICC;
      case "PIV", "IV", "PERIPHERAL" -> GenData.IV_ACCESS_PERIPHERAL_IV;
      case "PORT" -> GenData.IV_ACCESS_PORT;
      case "HICKMAN" -> GenData.IV_ACCESS_HICKMAN;
      case "MIDLINE" -> GenData.IV_ACCESS_MIDLINE;
      default -> (text.contains("access port")) ? GenData.IV_ACCESS_PORT : null;
    };
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

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<Patient> patients = new PatientImporter().process();
    patients.forEach(System.out::println);
    System.out.printf("Total size: %d\n By status: %s\n", patients.size(),
        patients.stream().collect(Collectors.groupingBy(Patient::getStatus, Collectors.counting())));

    patients.parallelStream()
        .peek(p -> System.out.printf("Creating patient: %s %s\n", p.getId(), p.getName()))
        .forEach(dao::create);
  }
}
