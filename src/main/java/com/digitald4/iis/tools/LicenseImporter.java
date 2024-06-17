package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.parseDate;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.storage.GenData;
import com.digitald4.iis.storage.NurseStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

public class LicenseImporter implements DataImporter<License> {
  private ImmutableList<String> columnNames;
  private final ImmutableMap<String, GeneralData> licenseTypeByName;
  private final ImmutableMap<String, Nurse> nursesByName;

  public LicenseImporter(GeneralDataStore generalDataStore, NurseStore nurseStore) {
    licenseTypeByName = generalDataStore.listByGroupId(GenData.LICENSE).getItems().stream()
        .flatMap(licenseGroup -> generalDataStore.listByGroupId(licenseGroup.getId()).getItems().stream())
        .collect(toImmutableMap(GeneralData::getName, Function.identity()));
    nursesByName = nurseStore.list(Query.forList()).getItems().stream()
        .collect(toImmutableMap(n -> n.fullName().replaceAll("\"", ""), Function.identity()));
  }

  @Override
  public LicenseImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    System.out.println(columnNames);
    return this;
  }

  @Override
  public ImmutableList<License> process() {
    return process("data/Employee Expiring Credentials.csv");
  }

  @Override
  public ImmutableList<License> process(String filePath) {
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<License> licenses = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        License license = parse(line);
        if (license != null) {
          licenses.add(license);
        }
      }
      return licenses.build();
    } catch (IOException | ArrayIndexOutOfBoundsException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  @Override
  public License parse(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    long licTypeId = parseLicenseType(licenseTypeByName, json.getString("Description"));
    return licTypeId == 0 ? null : new License()
        .setNurseId(parseNurse(nursesByName, json.getString("Employee")))
        .setLicTypeId(licTypeId)
        .setExpirationDate(parseDate(json.getString("Date")));
  }

  public static long parseNurse(ImmutableMap<String, Nurse> nursesByName, String nurseName) {
    Nurse nurse = nursesByName.get(nurseName);
    if (nurse == null) {
      throw new DD4StorageException("Unknown nurse: " + nurseName);
    }
    return nurse.getId();
  }

  public static long parseLicenseType(ImmutableMap<String, GeneralData> licenseTypeByName, String licenseName) {
    licenseName = switch (licenseName) {
      case "Annual PPD", "Chest Xray" -> "TB Test/PPD/Chest X-ray";
      case "Annual Review" -> "Annual Evaluation";
      case "Annual TB Questionnaire" -> "TB Questionnaire";
      case "BLS/CPR" -> "BLS/CPR Card";
      case "Driver's License" -> "Driver License";
      case "Flu Vaccine", "Flu Waiver" -> "Flu Vaccine / Waiver";
      case "Malpractice $1,000,000,000 minimum policy" -> "Malpractice/Professional Liability Insurance";
      case "Physical" -> "Cleared Physical / Medical Release";
      default -> licenseName;
    };
    GeneralData licenseType = licenseTypeByName.get(licenseName);
    if (licenseType == null) {
      System.out.printf("Unknown license type: %s\n", licenseName);
      return 0;
    }
    return licenseType.getId();
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    NurseStore nurseStore = new NurseStore(() -> dao, null);
    GeneralDataStore generalDataStore = new GeneralDataStore(() -> dao);
    ImmutableList<License> licenses =
        new LicenseImporter(generalDataStore, nurseStore).process();
    // licenses.forEach(System.out::println);
    System.out.printf("Total size: %d\n", licenses.size());
    licenses.stream()
        .peek(l -> System.out.println("Inserting license: " + l))
        .forEach(dao::create);
  }
}
