package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.assertEmpty;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.Query;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AccountingInfo.AccountingType;
import com.digitald4.iis.model.Employee;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AppointmentImporter implements DataImporter<Appointment> {
  private ImmutableList<String> columnNames;

  @Override
  public AppointmentImporter setColumnNames(String line) {
    System.out.println(line);
    ImmutableList<String> columnNames = Calculate.splitCSV(line);
    HashSet<String> usedNames = new HashSet<>();
    this.columnNames = columnNames.stream()
        .map(name -> !usedNames.contains(name) ? name : name + "_1").peek(usedNames::add).collect(toImmutableList());
    System.out.println(columnNames);
    return this;
  }

  @Override
  public ImmutableList<Appointment> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Service - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<Appointment> process(String filePath) {
    System.out.println("Importing file: " + filePath);
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
        appointments.add(parse(line));
      }
      return appointments.build().stream()
          .sorted(comparing(Appointment::start, reverseOrder())).collect(toImmutableList());
    } catch (Exception e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  @Override
  public Appointment parse(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Cancel/ Hold", "Designation", "Break", "Punch  In  Distance", "Punch  Out  Distance",
        "Visit  Tags", "Funded", "Funder Status", "Validation Code", "Last Update");
    return new Appointment()
        .setId(json.getLong("Visit ID"))
        .setStart(parseDateTime(json.getString("Start (UTC) Time")))
        .setEnd(parseDateTime(json.getString("End (UTC) Time")))
        .setPatientId(json.getLong("Internal Id"))
        .setPatientName(json.optString("Full Name"))
        .setNurseId(json.getLong("Internal Id_1"))
        .setNurseName(json.getString("Full Name_1"))
        .setVendorId(json.getLong("Id"))
        .setVendorName(json.getString("Name"))
        .setTimeIn(parseDateTime(json.optString("Punch Start Time")))
        .setTimeOut(parseDateTime(json.optString("Punch End Time")))
        .setAssessmentApproved("Approved".equals(json.optString("Approval Status")))
        .setNurseConfirmNotes(json.optString("Notes", null))
        .setBillingInfo(parseAccountingInfo(json.optDouble("Bill Duration", 0.0), json.optDouble("Bill Override"), null))
        .setPaymentInfo(parseAccountingInfo(json.optDouble("Hours Approved", 0.0), json.optDouble("Pay Override"), null))
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

  public static Instant parseDateTime(String value) throws ParseException {
    return isNullOrEmpty(value) ? null
        : Instant.ofEpochMilli(FormatText.parseDate(value, FormatText.MYSQL_DATETIME).getTime());
  }

  public static void deleteBatch(DAO dao, int limit) {
    System.out.printf("Deleting %d appointments...", limit);
    int deleted = dao.delete(Appointment.class,
        dao.list(Appointment.class, Query.forList().setLimit(limit)).getItems().stream()
            .map(Appointment::getId).collect(toImmutableList()));
    System.out.printf("%d appointments deleted\n", deleted);
  }

  public static void create(DAO dao, int batchDay) {
    ImmutableList<Appointment> appointments = new AppointmentImporter().process();
    appointments.forEach(System.out::println);
    ImmutableSet<Long> nurseIds = new EmployeeImporter().process().stream()
        .filter(e -> e instanceof Nurse).map(Employee::getId).collect(toImmutableSet());
        // nurseStore.list(Query.forList()).getItems().stream().map(Nurse::getId).collect(toImmutableSet());
    ImmutableSet<Long> patientIds =
        new PatientImporter().process().stream().map(Patient::getId).collect(toImmutableSet());
    ImmutableList<Appointment> filtered = appointments.stream()
        .filter(a -> nurseIds.contains(a.getNurseId()))
        .filter(a -> patientIds.contains(a.getPatientId()))
        .collect(toImmutableList());
    System.out.printf("Total size: %d, Filtered count: %d\n", appointments.size(), filtered.size());

    if (batchDay > 0) {
      int dayLimit = 22000;
      int batchSize = 1000;
      int batches = dayLimit / batchSize;
      IntStream.range(0, batches)
          .map(batch -> (batchDay - 1) * dayLimit + batch * batchSize)
          .mapToObj(start -> Pair.of(start, Math.min(start + batchSize, filtered.size())))
          .peek(pair -> System.out.printf("Inserting records %d to %d\n", pair.getLeft(), pair.getRight()))
          .forEach(pair -> dao.create(filtered.subList(pair.getLeft(), pair.getRight())));
      /* AtomicInteger index = new AtomicInteger(batchSize * (batchDay - 1));
      filtered.stream().parallel()
          .skip(index.get())
          .limit(batchSize)
          .peek(a -> System.out.printf("%d Inserting appointment: %s\n", index.getAndIncrement(), a.getId()))
          .forEach(dao::create); */
    }
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    // IntStream.range(0, 5).forEach(i -> deleteBatch(dao, 4096));
    create(dao, 3);
  }
}
