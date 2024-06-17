package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.parseDate;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.Query;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.model.Vendor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BillCodeImporter implements DataImporter<ServiceCode> {
  private final ImmutableMap<Long, String> vendorNames;

  public BillCodeImporter(ImmutableMap<Long, String> vendorNames) {
    this.vendorNames = vendorNames;
  }

  @Override
  public BillCodeImporter setColumnNames(String line) {
    throw new UnsupportedOperationException("Unsupported");
  }

  @Override
  public ImmutableList<ServiceCode> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Bill Code - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<ServiceCode> process(String filePath) {
    AtomicInteger lineNum = new AtomicInteger(1);
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      StringBuilder content = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line).append("\n");
        lineNum.incrementAndGet();
      }

      lineNum.set(1);
      return Calculate.jsonFromCSV(content.toString()).stream()
          .peek(n -> lineNum.incrementAndGet())
          .map(this::parse)
          .filter(Objects::nonNull)
          .collect(toImmutableList());
    } catch (Exception e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum, e);
    }
  }

  @Override
  public ServiceCode parse(String line) throws ParseException {
    throw new UnsupportedOperationException("Unsupported");
  }

  public ServiceCode parse(JSONObject json) {
    if (json.optString("Code", null) == null || json.getString("Units").equals("rate")) {
      return null;
    }

    try {
      return (ServiceCode) new ServiceCode()
          .setType(ServiceCode.Type.Bill)
          .setVendorId(json.getLong("Id_1"))
          .setCode(json.getString("Code"))
          .setVendorName(vendorNames.get(json.getLong("Id_1")))
          .setUnitPrice(json.getDouble("Bill Rate"))
          .setUnit(parseUnit(json.getString("Units")))
          .setDescription(json.getString("Description"))
          .setCreationTime(parseDate(json.optString("Creation Date")));
    } catch (Exception e) {
      throw new DD4StorageException("Error parsing json: " + json, e);
    }
  }

  public static ServiceCode.Unit parseUnit(String unit) {
    return ServiceCode.Unit.valueOf(FormatText.toCapitalized(unit.substring(0, unit.length() - 1)));
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableMap<Long, String> vendorNames = dao.list(Vendor.class, Query.forList().setLimit(200)).getItems().stream()
        .collect(toImmutableMap(Vendor::getId, Vendor::getName));
    ImmutableList<ServiceCode> billCodes = new BillCodeImporter(vendorNames).process();
    billCodes.forEach(System.out::println);
    System.out.printf("Total size: %d\n", billCodes.size());
    if (billCodes.size() != ImmutableSet.copyOf(billCodes).size()) {
      throw new DD4StorageException(
          "Size missmatch " + ImmutableSet.copyOf(billCodes).size() + " != "  + billCodes.size());
    }
    billCodes.stream().peek(billCode -> System.out.printf("Creating %s\n", billCode)).forEach(dao::create);
  }
}
