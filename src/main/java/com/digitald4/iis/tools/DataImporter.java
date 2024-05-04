package com.digitald4.iis.tools;

import static java.util.Arrays.stream;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.Phone;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.storage.GenData;
import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.time.Instant;
import org.json.JSONObject;

/*
Order, first download Employee - All Time, Client - All Time, Client-Funder Association, Funder - All Time.
Download all to data/ as csv and select all results.
Run Employee Importer, Vendor Importer, Client Importer then Appointment Importer.
 */

public interface DataImporter<T> {
  DataImporter<T> setColumnNames(String line);
  ImmutableList<T> process();
  ImmutableList<T> process(String filename);
  T parse(String line) throws ParseException;

  static Phone parsePhone(String text) {
    if (text == null) {
      return null;
    }

    Phone phone = new Phone();
    if (text.contains(" ")) {
      String type = text.substring(0, text.indexOf(" "));
      text = text.substring(text.indexOf(" ") + 1);
      switch (type) {
        case "Home" -> phone.setTypeId(GenData.PHONE_TYPE_HOME);
        case "Mobile" -> phone.setTypeId(GenData.PHONE_TYPE_MOBILE);
        case "Work" -> phone.setTypeId(GenData.PHONE_TYPE_WORK);
      }
    }
    return phone.setNumber(text);
  }

  static Address parseAddress(
      String address, String city, String state, String zip, String unit, double lat, double lon) {
    return new Address()
        .setAddress(String.format("%s%s%s%s", address,
            city.isEmpty() ? "" : " " + city, state.isEmpty() ? "" : ", " + state, zip.isEmpty() ? "" : ", " + zip))
        .setUnit(unit)
        .setLatitude(lat)
        .setLongitude(lon);
  }

  static Address parseAddress(String address, String city, String state, String zip, String unit) {
    return parseAddress(address, city, state, zip, unit, 0, 0);
  }

  static Address parseAddress(JSONObject json) {
    return parseAddress(
        json.optString("Address"), json.optString("City"), json.optString("State"), json.optString("Zip Code"),
        json.optString("Address Line 2", null), json.optDouble("Address Latitude", 0), json.optDouble("Address Longitude", 0));
  }

  static Instant parseDate(String value) throws ParseException {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return Instant.ofEpochMilli(
        FormatText.parseDate(value, value.contains("/") ? FormatText.USER_DATE : FormatText.MYSQL_DATE).getTime());
  }

  public static void assertEmpty(JSONObject json, String... colNames) {
    stream(colNames).filter(json::has)
        .filter(colName -> !(json.get(colName).equals(" - ") || json.getString(colName).equals("0") || json.getString(colName).equals("N/A")))
        .forEach(colName -> System.out.printf("Found value '%s' in column: %s\n", json.get(colName), colName));
  }
}
