package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.*;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.model.Vendor.Status;
import com.google.common.collect.ImmutableList;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;

public class VendorImporter implements DataImporter<Vendor> {
  private ImmutableList<String> columnNames;

  @Override
  public VendorImporter setColumnNames(String line) {
    System.out.println(line);
    columnNames = Calculate.splitCSV(line);
    return this;
  }

  @Override
  public ImmutableList<Vendor> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Funder - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<Vendor> process(String filePath) {
    System.out.println("Importing file: " + filePath);
    String line = null;
    int lineNum = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      ImmutableList.Builder<Vendor> vendors = ImmutableList.builder();
      setColumnNames(br.readLine());
      while ((line = br.readLine()) != null) {
        lineNum++;
        vendors.add(parse(line));
      }
      return vendors.build();
    } catch (IOException | ArrayIndexOutOfBoundsException | ParseException e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum + " " + line, e);
    }
  }

  @Override
  public Vendor parse(String line) throws ParseException {
    JSONObject json = Calculate.jsonFromCSV(columnNames, line);
    assertEmpty(json, "Groups", "Tags V2", "Social Security Number", "Communication Method", "Services  Erl");
    return new Vendor()
        .setId(json.getLong("Id"))
        .setName(json.getString("Name"))
        .setStatus("No".equals(json.getString("Is Disabled (Yes / No)")) ? Status.ACTIVE : Status.IN_ACTIVE)
        .setAddress(parseAddress(json))
        .setContactEmail(json.optString("Email", null))
        .setHolidayMultiplier(json.getDouble("Holiday Multiplier"))
        .setInvoicingModel(Vendor.InvoicingModel.valueOf(json.getString("Type").replace(" ", "_")));
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<Vendor> vendors = new VendorImporter().process();
    vendors.forEach(System.out::println);
    System.out.printf("Total size: %d\n", vendors.size());
    vendors.stream()
        .peek(v -> System.out.printf("Creating vendor: %s %s\n", v.getId(), v.getName()))
        .forEach(dao::create);
  }
}
