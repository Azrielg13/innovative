package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.PatientImporter.assertEmpty;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AccountingInfo.AccountingType;
import com.digitald4.iis.storage.NurseStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.time.Instant;

public class AppointmentImporter {
  private ImmutableList<String> columnNames;
  private final GeneralDataStore generalDataStore;
  private final NurseStore nurseStore;

  public AppointmentImporter(GeneralDataStore generalDataStore, NurseStore nurseStore) {
    this.generalDataStore = generalDataStore;
    this.nurseStore = nurseStore;
  }

  public AppointmentImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    System.out.println(columnNames);
    return this;
  }

  public ImmutableList<Appointment> process(Iterable<String> filePaths) {
    return Streams.stream(filePaths)
        .flatMap(filePath -> {
          String line = null;
          int lineNum = 1;
          try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            ImmutableList.Builder<Appointment> appointments = ImmutableList.builder();
            setColumnNames(br.readLine());
            while ((line = br.readLine()) != null) {
              lineNum++;
              // System.out.println(line);
              if (line.trim().length() < 15) {
                continue;
              }
              Appointment appointment = parseAppointment(line);
              if (appointment != null) {
                appointments.add(appointment);
              }
            }
            return appointments.build().stream();
          } catch (Exception e) {
            throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
          }
        })
        .collect(toImmutableList());
  }

  public Appointment parseAppointment(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Cancel/ Hold", "Designation", "Break", "Punch  In  Distance", "Punch  Out  Distance",
        "Visit  Tags", "Funded", "Funder Status", "Validation Code", "Last Update");
    return new Appointment()
        .setPatientId(Long.parseLong(json.getString("Account Id").substring(1)))
        .setNurseId(Long.parseLong(json.getString("Employee Id").substring(1)))
        .setStart(parseDate(json.getString("Date") + " " + json.getString("In")))
        .setEnd(parseDate(json.getString("Date") + " " + json.getString("Out")))
        .setAssessmentApproved(json.has("Status") && json.getString("Status").equals("Approved"))
        .setNurseConfirmNotes(json.optString("Notes"))
        .setBillingInfo(parseAccountingInfo(
            json.optDouble("Approved Hours"), json.optDouble("Bill-Override"), json.optString("Bill-Override Units")))
        .setPaymentInfo(parseAccountingInfo(
            json.optDouble("Approved Hours"), json.optDouble("Pay-Override"), json.optString("Pay-Override Units")))
        ;
  }

  public static AccountingInfo parseAccountingInfo(double hours, double hoursOverride, String unit) {
    if (hours == 0.0 && hoursOverride == 0.0 && unit == null) {
      return null;
    }
    return new AccountingInfo()
        .setAccountingType("Visits".equals(unit) ? AccountingType.Fixed : AccountingType.Hourly)
        .setHours(hoursOverride == 0 ? hours : hoursOverride);
  }

  public static Instant parseDate(String value) throws ParseException {
    return value == null || value.isEmpty()
        ? null : Instant.ofEpochMilli(FormatText.parseDate(value, FormatText.USER_DATETIME).getTime());
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    NurseStore nurseStore = new NurseStore(() -> dao);
    GeneralDataStore generalDataStore = new GeneralDataStore(() -> dao);
    ImmutableList<Appointment> appointments = new AppointmentImporter(generalDataStore, nurseStore).process(
        stream(new File("data/").list()).filter(f -> f.startsWith("Schedule")).map(f -> "data/" + f)
            .collect(toImmutableList()));
    // licenses.forEach(System.out::println);
    System.out.printf("Total size: %d\n", appointments.size());
    /* appointments.stream()
        .peek(a -> System.out.println("Inserting appointment: " + a))
        .forEach(dao::create); */
  }
}
