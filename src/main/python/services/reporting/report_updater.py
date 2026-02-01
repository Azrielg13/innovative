import json
import os
import pandas as pd
import re
import sheets_api
from datetime import datetime
from dd4_service import DD4Service

ONE_MINUTE = 60 * 1000
ONE_HOUR = ONE_MINUTE * 60
ONE_DAY = ONE_HOUR * 24
ONE_MONTH = ONE_DAY * 31
MONTH_NAMES = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
               'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
APPOINTMENT_FIELDS = ["id", "patientId", "date", "vendorName", "loggedHours",
                      "billingInfo", "paymentInfo", "status"]
PATIENT_FIELDS = [
  "id", "creationTime", "status", "referralResolutionDate", "referralResolution",
  "billingVendorId", "billingVendorName", "condition", "firstAppointmentDate"]
SPREADSHEET_2024_TEST = '1j6W4t7N__QdKwBAHKkHFdQC0SEdHgqnhkRtfsh9d9LQ'
SPREADSHEET_2024 = '1cjib9KvuMBRktL6bNdlZin1RkNiCk2F5PlvKIbXmdsk'
SPREADSHEET_2025 = '1URkUKK8hsbl-z-uzZ4tE66eUoNZ6P9o9F2GDY4I0crs'
SPREADSHEET_2025_VERIFY = '1vAtQ9ZGMo8cBIubuW4LK2n8EhtJ_MqGAG9sa17-kxXo'
SPREADSHEET_2026 = '1FCjeoNA287Hpr4g-M3oAGNx3-kMsPZbpIxWQZdO78zA'

class ReportUpdater:
  def __init__(self, spreadsheet_id: str, id_token: str=None, use_cache_file: bool=False):
    self.spreadsheet_id = spreadsheet_id
    spreadsheet = sheets_api.get(self.spreadsheet_id)
    self.title = spreadsheet['properties']['title']
    pattern = r'^(.*)\s(\d{4})\s*(\(test\))?$'
    match = re.match(pattern, self.title)
    self.type = match.group(1).strip()
    self.year = int(match.group(2))
    self.is_test = bool(match.group(3))
    self.dd4_service = DD4Service(id_token, self.is_test)
    self.cached_reader = CachedReader(
        self.dd4_service, self.year, use_cache_file and self.year < 2025)

  def get_config(self) -> dict:
    return {
      "title": self.title,
      "year": self.year,
      "type": self.type,
      "test": self.is_test
    }

  def new_referrals_acceptance(self):
    df = self.cached_reader.get_data('patients')

    # ---- Ensure required columns exist ----
    required = ["id", "creationTime", "referralResolutionDate",
                "referralResolution", "billingVendorName", "condition"]
    for col in required:
      if col not in df.columns:
        df[col] = None

    df["creation"] = df["creationTime"].fillna(0).astype(int)
    df["creation_dt"] = pd.to_datetime(df["creation"], unit="ms", errors="coerce")
    df["rt"] = df["referralResolutionDate"].fillna(0).astype(int)

    df["month"] = df["creation_dt"].dt.month.apply(
        lambda m: MONTH_NAMES[m - 1] if pd.notna(m) else ""
    )

    df.loc[df["creation_dt"].dt.year != self.year, "month"] = (
        df["creation_dt"].dt.year.astype(str) + "-" + df["month"]
    )
    df["processing_mins"] = df.apply(
        lambda r: round((r.rt - r.creation) / ONE_MINUTE, 1)
        if r.rt > 0 else "",
        axis=1
    )

    result_df = df[["id", "creation", "month", "referralResolution", "rt",
                    "processing_mins", "billingVendorName", "condition"]]
    resolution_infos = result_df.fillna("").values.tolist()

    print(resolution_infos)
    resolution_infos.extend([[''] * 8] * 100)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": "'Referrals Data'!A1:H1", "values": [['Patient Id', 'Referral Ts', 'Month', 'Resolution', 'Resolution Ts', 'Processing Time (Mins)', 'Client', 'Referral Type']]},
          {"range": f"'Referrals Data'!A2:H{len(resolution_infos) + 2}", "values": resolution_infos},
        ])

  def output_appointment_info(self):
    appts = self.cached_reader.get_data('appointments').copy()
    appts = appts[appts["patientId"].notna()]
    patients = self.cached_reader.get_data('patients').copy()

    # Join patient data
    df = appts.merge(
        patients[["id", "firstAppointmentDate"]],
        left_on="patientId",
        right_on="id",
        how="left",
        suffixes=("", "_patient")
    )

    print(df['billingInfo.total'].describe())

    df["date"] = df["date"].astype(int)
    df["date_dt"] = pd.to_datetime(df["date"], unit="ms", errors="coerce")
    df["month"] = df["date_dt"].dt.month.apply(
        lambda m: MONTH_NAMES[m - 1] if pd.notna(m) else ""
    )

    df["first_appt_dt"] = pd.to_datetime(
        df["firstAppointmentDate"].fillna(0).astype(int),
        unit="ms",
        errors="coerce"
    )

    df["is_new_hours"] = (
        (df["first_appt_dt"].dt.month == df["date_dt"].dt.month) &
        (df["first_appt_dt"].dt.year == df["date_dt"].dt.year)
    ).map({True: "TRUE", False: ""})

    result_df = df[["id", "patientId", "date", "month", "vendorName", "loggedHours",
                    "is_new_hours", "billingInfo.total", "paymentInfo.total"]]

    appointment_rows = result_df.fillna("").values.tolist()
    appointment_rows.extend([[''] * 9] * 100)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": "'Appointment Data'!A1:K1", "values": [
            ['Appointment Id', 'Patient Id', 'Date', 'Month', 'Client', 'Hours',
             'New Hours', 'Billing Total', 'Payment Total']]},
          {"range": f"'Appointment Data'!A2:K{len(appointment_rows) + 2}", "values": appointment_rows},
        ])

  def output_vendor_info(self):
    vendors = self.cached_reader.get_data('vendors')

    vendor_names = [[n] for n in vendors["name"].dropna().sort_values()]

    print(vendor_names)
    vendor_names.extend([['']] * 100)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": f"'Referrals by Client'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
          {"range": f"'# of Visits'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
        ])

  def output_missmatch_invoices(self):
    invoices = self.cached_reader.get_data("invoices").copy()
    appointments = self.cached_reader.get_data("appointments").copy()

    print(f"Total of {len(invoices)} invoices found")

    inv_apps = invoices[["id", "appointmentIds", "totalDue", "date", "vendorName"]] \
      .explode("appointmentIds") \
      .rename(columns={"id": "invoice_id", "appointmentIds": "appointment_id"})

    merged = inv_apps.merge(
        appointments[["id", "billingInfo.total"]],
        left_on="appointment_id",
        right_on="id",
        how="left"
    )

    merged["billingInfo.total"] = merged["billingInfo.total"].fillna(0)

    invoice_totals = (
      merged
      .groupby("invoice_id", as_index=False)
      .agg({
        "billingInfo.total": "sum",
        "totalDue": "first",
        "date": "first",
        "vendorName": "first",
      })
    )

    mismatches = invoice_totals[
      invoice_totals["billingInfo.total"].round(2)
      != invoice_totals["totalDue"].astype(float).round(2)
    ]

    mismatches["date"] = pd.to_datetime(
        mismatches["date"].astype(int),
        unit="ms",
        errors="coerce"
    ).dt.strftime("%-m/%-d/%Y")

    output = mismatches[[
      "invoice_id",
      "date",
      "vendorName",
      "totalDue",
      "billingInfo.total",
    ]].fillna("")

    miss_matches = output.values.tolist()

    miss_matches.extend([[''] * 5] * 100)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": "'Missmatch Invoices'!A1:F1",
           "values": [['Invoice', 'Date', 'Vendor', 'Billed', 'Appointment Total']]},
          {"range": f"'Missmatch Invoices'!A2:F{len(miss_matches) + 2}",
           "values": miss_matches},
        ])

  def update(self) -> dict:
    self.output_vendor_info()
    self.new_referrals_acceptance()
    self.output_appointment_info()
    self.output_missmatch_invoices()
    # Update the title in case it has changed, the system will update last modified.
    return self.dd4_service.update(
        'reports',{'id': self.spreadsheet_id, 'title': self.title},
        ['title']).iloc[0].to_dict()


class CachedReader:
  def __init__(self, dd4_service, year, use_file_io=False):
    self.dd4_service = dd4_service
    self.year = year
    self.use_file_io = use_file_io
    self.cached_data = {}

  def get_data(self, type: str) -> list:
    year = self.year
    cache_id = f"{'test-' if self.dd4_service.is_test else ''}{year}-{type}"
    file_path = f'data/{cache_id}.json'

    if self.use_file_io and os.path.isfile(file_path):
      with open(file_path, "r", encoding="utf-8") as f:
        self.cached_data[cache_id] = list(map(json.loads, f))
        return self.cached_data[cache_id]

    dfs = []
    if type == 'patients':
      dfs = [
        self.dd4_service.list('patients', fields=PATIENT_FIELDS,
                              filters=['status!=Discharged'], page_size=2750),
        self.dd4_service.list('patients', fields=PATIENT_FIELDS,
                              filters=['status=Discharged'], page_size=2750)]
    elif type == 'vendors':
      dfs = [self.dd4_service.list('vendors', page_size=2750)]
    elif type == 'invoices':
      s = int(datetime(year, 1, 1, 0, 0, 0).timestamp() * 1000)
      e = int(datetime(year + 1, 1, 1, 0, 0, 0).timestamp() * 1000)
      dfs = [self.dd4_service.list('invoices', filters=[f'date%3E{s},date%3C{e}'], page_size=2750)]
    elif type == 'appointments':
      dfs = [
        self.dd4_service.list(
            "appointments",
            fields=APPOINTMENT_FIELDS,
            filters=[
              f"start%3E{int(datetime(year, m, 1).timestamp() * 1000)},"
              f"start%3C{int(datetime(year if m < 12 else year + 1, m + 1 if m < 12 else 1, 1).timestamp() * 1000)}"
            ],
            page_size=2750
        )
        for m in range(1, 13)
      ]
    elif type == 'patient_histories':
      dfs = [
        self.dd4_service.list(
            "changeHistorys",
            filters=[
              'entityType=Patient','action=UPDATED'
              f"timeStamp%3E{int(datetime(year, m, 1).timestamp() * 1000)},"
              f"timeStamp%3C{int(datetime(year if m < 12 else year + 1, m + 1 if m < 12 else 1, 1).timestamp() * 1000)}"
            ],
            order_by='timeStamp',
            page_size=2750
        )
        for m in range(1, 13)
      ]
    else:
      raise ValueError(f'Unknown type: {type}')

    df = pd.concat(dfs, ignore_index=True)

    if self.use_file_io:
      with open(file_path, "w", encoding="utf-8") as f:
        for e in df:
          json.dump(e, f)
          f.write("\n")

    return df


if __name__ == "__main__":
  with open('data/dd4_token.txt', 'r') as f:
    id_token = f.readline()

  # sheets_api.create('Financial & Referral KPI 2024')
  # sheets_api.copy_file(spreadsheet_id, 'Financial & Referral KPI 2025')
  report_updater = ReportUpdater(SPREADSHEET_2025_VERIFY, id_token, False)
  # report_updater = ReportUpdater(SPREADSHEET_2024_TEST, id_token, False)
  # report_updater = ReportUpdater(SPREADSHEET_2026, id_token, False)
  print(report_updater.update())
