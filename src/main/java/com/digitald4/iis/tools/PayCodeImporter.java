package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.parseDate;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Arrays.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.ServiceCode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public class PayCodeImporter implements DataImporter<ServiceCode> {

  @Override
  public PayCodeImporter setColumnNames(String line) {
    throw new UnsupportedOperationException("Unsupported");
  }

  @Override
  public ImmutableList<ServiceCode> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Service Code - All Time"))
        .map(f -> "data/" + f).max(Comparator.comparing(Objects::toString)).orElseThrow());
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
    if (json.optString("Rate", null) == null || json.getString("Units") == null) {
      return null;
    }

    try {
      return (ServiceCode) new ServiceCode()
          .setType(ServiceCode.Type.Pay)
          .setCode(json.getString("Name"))
          .setUnitPrice(json.getDouble("Rate"))
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
    // test-dot-ip360-179401.uc.r.appspot.com
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<ServiceCode> serviceCodes = new PayCodeImporter().process();
    serviceCodes.forEach(System.out::println);
    System.out.printf("Total size: %d\n", serviceCodes.size());
    if (serviceCodes.size() != ImmutableSet.copyOf(serviceCodes).size()) {
      throw new DD4StorageException(
          "Size missmatch " + ImmutableSet.copyOf(serviceCodes).size() + " != "  + serviceCodes.size());
    }
    serviceCodes.stream().peek(serviceCode -> System.out.printf("Creating %s\n", serviceCode)).forEach(dao::create);
  }
}
