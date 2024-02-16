package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.PatientImporter.getValue;
import static com.digitald4.iis.tools.PatientImporter.parseAddress;
import static com.digitald4.iis.tools.PatientImporter.parseDate;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.model.Employee;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Nurse.Status;
import com.digitald4.iis.storage.NurseStore;
import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import org.json.JSONObject;

public class EmployeeImporter {
  private ImmutableList<String> columnNames;

  public EmployeeImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    System.out.println(columnNames);
    return this;
  }

  public ImmutableList<Employee> importNurses(String filePath) {
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
    } catch (IOException | ArrayIndexOutOfBoundsException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  public Nurse parseEmployee(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    return new Nurse()
        .setId(Long.parseLong(json.getString("Uid").substring(1)))
        .setFirstName(json.optString("Name"))
        .setLastName(json.optString("Last  Name"))
        .setStatus(Status.valueOf(json.getString("Status")))
        .setTimeZone(json.getString("Time  Zone"))
        .setPhoneNumber(json.optString("Phone"))
        .setAddress(parseAddress(
            json.optString("Address"), json.optString("City"), json.optString("State"),
            json.optString("Zip"), getValue(json, "Address2")))
        .setEmail(json.optString("Email"))
        .setDateOfBirth(parseDate(getValue(json, "Date Of Birth")))
        .setHireDate(parseDate(getValue(json, "Hire Date")));
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(
        new APIConnector("https://ip360-179401.appspot.com/_api", "v1").setIdToken("614463970"));
    NurseStore nurseStore = new NurseStore(() -> dao);
    // nurseStore.create(new Patient().setId(123L).setName("Code Test User"));
    ImmutableList<Employee> employees = new EmployeeImporter().importNurses("data/employee-list.csv");
    employees.forEach(System.out::println);
    System.out.printf("Total size: %d\n", employees.size());
    /* nurses.stream()
        .forEach(p -> System.out.printf("Creating patient: %s %s\n", p.getId(), p.fullName()));
        .forEach(nurseStore::create); */
  }
}
