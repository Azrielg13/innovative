package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.*;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.model.Employee;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.User;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeImporter implements DataImporter<Employee> {
  private ImmutableList<String> columnNames;

  @Override
  public EmployeeImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    return this;
  }

  @Override
  public ImmutableList<Employee> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Employee - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<Employee> process(String filePath) {
    System.out.println("Importing file: " + filePath);
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<Employee> employees = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        employees.add(parse(line));
      }
      return employees.build();
    } catch (IOException | ArrayIndexOutOfBoundsException | JSONException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  @Override
  public Employee parse(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Designation");
    String title = json.optString("Title");
    if (title.isEmpty()) {
      title = json.optString("Job Title");
    }

    return isNurse(title)
        ? parsePayRate(
            new Nurse()
                .setId(json.getLong("Internal Id"))
                .setFirstName(json.optString("First Name"))
                .setLastName(json.optString("Last Name"))
                .setStatus(parseStatus(json.getString("Status")))
                .setTimeZone(json.optString("Time Zone"))
                .setPhoneNumber(json.optString("Phone (Main)", null))
                .setAddress(parseAddress(json))
                .setEmail(json.optString("Email", null))
                .setHireDate(parseDate(json.optString("Start Date", null)))
                .setDateOfBirth(parseDate(json.optString("Birthday", null)))
                .setHireDate(parseDate(json.optString("Hire Date", null)))
                .setNotes(String.format("Pay Rate: %s\n", json.optString("Pay Rate"))),
            json.optString("Pay Rate", null))
        : new User()
            .setId(json.getLong("Internal Id"))
            .setJobTitle(json.optString("Job Title"))
            .setDepartment(json.optString("Departments"))
            .setFirstName(json.optString("First Name"))
            .setLastName(json.optString("Last Name"))
            .setStatus(parseStatus(json.getString("Status")))
            .setTimeZone(json.optString("Time Zone"))
            .setPhoneNumber(json.optString("Phone (Main)", null))
            .setAddress(parseAddress(json))
            .setEmail(json.optString("Email", null))
            .setDateOfBirth(parseDate(json.optString("Birthday", null)))
            .setHireDate(parseDate(json.optString("Hire Date", null)));
  }

  private static boolean isNurse(String title) {
    return title.equals("CNA") || title.equals("Home Infusion Nurse") || title.equals("LVN")
        || title.equals("Registered Nurse") || title.equals("RN") || title.equals("EMT");
  }

  private static Employee.Status parseStatus(String status) {
    if ("on hold".equals(status)) {
      return Employee.Status.Hold;
    }
    if ("unknown".equals(status)) {
      return Employee.Status.Applicant;
    }
    return Employee.Status.valueOf(status.substring(0, 1).toUpperCase() + status.substring(1));
  }

  private static final Pattern PAY_RATE_PATTERN =
      Pattern.compile("[A-Za-z$ ]*(\\d+\\.*\\d+)[A-Za-z ]*/[A-Za-z$ ]*(\\d+\\.*\\d+)[A-Za-z$ ]*");
  private static final Pattern BASIC_PAY_WITH_MILEAGE = Pattern.compile("(\\d+\\.*\\d+)/(\\d+\\.*\\d+)/(.\\d+)");
  private static final Pattern BASIC_PAY = Pattern.compile("(\\d+\\.*\\d+)/(\\d+\\.*\\d+)");
  @VisibleForTesting static Nurse parsePayRate(Nurse nurse, String value) {
    if (value != null) {
      Matcher matcher = BASIC_PAY_WITH_MILEAGE.matcher(value);
      if (matcher.matches()) {
        return nurse.setPayRate(Double.parseDouble(matcher.group(1)))
            .setPayFlat(Double.parseDouble(matcher.group(2)))
            .setMileageRate(Double.parseDouble(matcher.group(3)));
      }

      matcher = BASIC_PAY.matcher(value);
      if (matcher.matches()) {
        return nurse.setPayRate(Double.parseDouble(matcher.group(1))).setPayFlat(Double.parseDouble(matcher.group(2)));
      }

      matcher = PAY_RATE_PATTERN.matcher(value);
      if (matcher.matches()) {
        return nurse.setPayRate(Double.parseDouble(matcher.group(1))).setPayFlat(Double.parseDouble(matcher.group(2)));
      }
    }
    return nurse;
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<Employee> employees = new EmployeeImporter().process();
    employees.forEach(System.out::println);
    int nurseCount = (int) employees.stream().filter(e -> e instanceof Nurse).count();
    System.out.printf("Total size: %d, nurses: %d\n", employees.size(), nurseCount);
    employees.stream().peek(e -> System.out.println("Creating employee: " + e)).forEach(dao::create);
  }
}
