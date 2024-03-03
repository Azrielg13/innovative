package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.PatientImporter.assertEmpty;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AccountingInfo.AccountingType;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.NurseStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class AppointmentImporter {
  private ImmutableList<String> columnNames;

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
              appointments.add(parseAppointment(line));
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
        .setAssessmentApproved("Approved".equals(json.optString("Status")))
        .setNurseConfirmNotes(json.optString("Notes", null))
        .setBillingInfo(parseAccountingInfo(
            json.optDouble("Approved Hours", 0.0), json.optDouble("Bill-Override"), json.optString("Bill-Override Units", null)))
        .setPaymentInfo(parseAccountingInfo(
            json.optDouble("Approved Hours", 0.0), json.optDouble("Pay-Override"), json.optString("Pay-Override Units", null)))
        ;
  }

  public static AccountingInfo parseAccountingInfo(Double hours, Double hoursOverride, String unit) {
    if (hours == 0.0 && hoursOverride.isNaN() && unit == null) {
      return null;
    }
    return new AccountingInfo()
        .setAccountingType("Visits".equals(unit) ? AccountingType.Fixed : AccountingType.Hourly)
        .setHours(hoursOverride.isNaN() ? hours : hoursOverride);
  }

  public static Instant parseDate(String value) throws ParseException {
    return isNullOrEmpty(value) ? null
        : Instant.ofEpochMilli(FormatText.parseDate(value, FormatText.USER_DATETIME).getTime());
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    NurseStore nurseStore = new NurseStore(() -> dao);
    ImmutableList<Appointment> appointments = new AppointmentImporter().process(
        stream(new File("data/").list()).filter(f -> f.startsWith("Schedule")).map(f -> "data/" + f)
            .collect(toImmutableList()));
    // appointments.forEach(System.out::println);
    System.out.printf("Total size: %d\n", appointments.size());
    ImmutableSet<Long> nurseIds =
        nurseStore.list(Query.forList()).getItems().stream().map(Nurse::getId).collect(toImmutableSet());
    ImmutableSet<Long> patientIds =
        new PatientImporter().process("data/client-list.csv").stream().map(Patient::getId).collect(toImmutableSet());
    ImmutableList<Appointment> filtered = appointments.stream()
        .filter(a -> nurseIds.contains(a.getNurseId()))
        .filter(a -> patientIds.contains(a.getPatientId()))
        .collect(toImmutableList());
    System.out.printf("Filtered count: %d\n", filtered.size());
    int batchSize = 18000;
    int batchDay = 1;
    AtomicInteger index = new AtomicInteger();
    filtered.stream()
        .skip(batchSize * batchDay)
        .limit(batchSize)
        .peek(a -> System.out.printf("%d Inserting appointment: %s\n", index.getAndIncrement(), a))
        .forEach(dao::create);
  }
}
