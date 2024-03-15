package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.PatientImporter.*;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Employee;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.User;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeImporter {
  private ImmutableList<String> columnNames;

  public EmployeeImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    return this;
  }

  public ImmutableList<Employee> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("employee-list")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  public ImmutableList<Employee> process(String filePath) {
    System.out.println("Importing file: " + filePath);
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<Employee> employees = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        employees.add(parseEmployee(line));
      }
      return employees.build();
    } catch (IOException | ArrayIndexOutOfBoundsException | JSONException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  public Employee parseEmployee(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Designation");
    String jobTitle = json.optString("Job  Title");
    if (jobTitle.isEmpty()) {
      jobTitle = json.optString("Role");
    }
    // System.out.println("Job Title: " + jobTitle);

    return jobTitle.equals("Home Infusion Nurse")
        ? new Nurse()
            .setId(Long.parseLong(json.getString("Uid").substring(1)))
            .setFirstName(json.optString("Name"))
            .setLastName(json.optString("Last  Name"))
            .setStatus(Employee.Status.valueOf(json.getString("Status")))
            .setTimeZone(json.optString("Time  Zone"))
            .setPhoneNumber(json.optString("Phone"))
            .setAddress(parseAddress(
                json.optString("Address"), json.optString("City"), json.optString("State"),
                json.optString("Zip"), json.optString("Address2", null)))
            .setEmail(json.optString("Email"))
            .setDateOfBirth(parseDate(json.optString("Date Of Birth", null)))
            .setHireDate(parseDate(json.optString("Hire Date", null)))
        : new User()
            .setJobTitle(json.optString("Job  Title"))
            .setDepartment(json.optString("Departments"))
            .setId(Long.parseLong(json.getString("Uid").substring(1)))
            .setFirstName(json.optString("Name"))
            .setLastName(json.optString("Last  Name"))
            .setStatus(Employee.Status.valueOf(json.getString("Status")))
            .setTimeZone(json.optString("Time  Zone"))
            .setPhoneNumber(json.optString("Phone"))
            .setAddress(parseAddress(
                json.optString("Address"), json.optString("City"), json.optString("State"),
                json.optString("Zip"), json.optString("Address2", null)))
            .setEmail(json.optString("Email"))
            .setDateOfBirth(parseDate(json.optString("Date Of Birth", null)))
            .setHireDate(parseDate(json.optString("Hire Date", null)));
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
