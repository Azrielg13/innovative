import json
import os
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
SPREADSHEET_2024_TEST = '1j6W4t7N__QdKwBAHKkHFdQC0SEdHgqnhkRtfsh9d9LQ'
SPREADSHEET_2024 = '1cjib9KvuMBRktL6bNdlZin1RkNiCk2F5PlvKIbXmdsk'
SPREADSHEET_2025 = '1URkUKK8hsbl-z-uzZ4tE66eUoNZ6P9o9F2GDY4I0crs'

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


  def to_patient_data_row(self, patient: dict) -> list:
    id = patient['id']
    creation = int(patient.get('creationTime') or 0)
    creation_datetime = datetime.fromtimestamp(creation / 1000)

    rt = int(patient.get('referralResolutionDate') or 0)

    month = MONTH_NAMES[creation_datetime.month - 1]
    if creation_datetime.year != self.year:
      month = f'{creation_datetime.year}-{month}'

    return [id, creation, month, patient.get('referralResolution'), rt,
            round((rt - creation) / ONE_MINUTE,1) if rt > 0 else '',
            patient.get('billingVendorName') or '', patient['condition']]
    # return [id, creation, month, 'Pending', None, None, patient.get('billingVendorName')]


  def new_referrals_acceptance(self):
    patients = self.cached_reader.get_data('patients')

    vendors = {}
    resolution_infos = []
    for patient in patients:
      resolution_info = self.to_patient_data_row(patient)
      # print(resolution_info)
      if resolution_info is None:
        continue
      resolution_infos.append(resolution_info)
      if resolution_info[6] is not None:
        vendors[resolution_info[6]] = [resolution_info[6]]

    print(resolution_infos)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": "'Referrals Data'!A1:H1", "values": [['Patient Id', 'Referral Ts', 'Month', 'Resolution', 'Resolution Ts', 'Processing Time (Mins)', 'Client', 'Referral Type']]},
          {"range": f"'Referrals Data'!A2:H{len(resolution_infos) + 2}", "values": resolution_infos},
        ])


  def to_appointment_row(self, appointment: dict) -> list:
    appointment['date'] = int(appointment['date'])
    date = datetime.fromtimestamp(appointment['date'] / 1000)
    month = MONTH_NAMES[date.month - 1]
    patient = appointment.get('patient')

    first_app = datetime.fromtimestamp(int(patient.get('firstAppointmentDate') or 0) / 1000)
    is_new_hours = 'TRUE' if first_app.month == date.month and first_app.year == date.year else ''

    billing_total, payment_total = 0, 0
    billing_info = appointment.get("billingInfo")
    if billing_info:
      billing_total = billing_info["total"]
    payment_info = appointment.get("paymentInfo")
    if payment_info:
      payment_total = payment_info["total"]

    return [appointment['id'], appointment['patientId'], appointment['date'],
            month, appointment.get('vendorName'), appointment.get('loggedHours'),
            is_new_hours, billing_total, payment_total]


  def output_appointment_info(self):
    patients = {p['id']: p for p in self.cached_reader.get_data('patients')}

    appointments = self.cached_reader.get_data('appointments')

    appointments = list(
        map(lambda a:{**a, 'patient':patients.get(a['patientId'])}, appointments))

    appointment_rows = list(map(self.to_appointment_row, appointments))

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

    vendor_names = list(sorted(map(lambda v:v['name'], vendors)))
    vendor_names = [[name] for name in vendor_names]

    print(vendor_names)
    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": f"'Referrals by Client'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
          {"range": f"'# of Visits'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
        ])


  def output_missmatch_invoices(self):
    invoices = self.cached_reader.get_data('invoices')
    appointments = {a['id']: a for a in self.cached_reader.get_data('appointments')}

    print(f'Total of {len(invoices)} invoices found')

    missmatches = []
    for invoice in invoices:
      total_due = 0
      for app_id in invoice['appointmentIds']:
        app = appointments.get(app_id)
        if app is not None:
          total_due = total_due + float(app['billingInfo']['total'])
        else:
          print(f'Appointment {app_id} not found')

      if total_due != float(invoice['totalDue']):
        print(f"Billing missmatch {invoice['totalDue']} != {total_due}")
        date = datetime.fromtimestamp(int(invoice['date']) / 1000)
        missmatches.append([invoice['id'], f'{date.month}/{date.day}/{date.year}', invoice.get('vendorName', ''), invoice['totalDue'], total_due])

    sheets_api.batch_update_values(
        self.spreadsheet_id,
        "USER_ENTERED",
        [
          {"range": "'Missmatch Invoices'!A1:F1", "values": [['Invoice', 'Date', 'Vendor', 'Billed', 'Appointment Total']]},
          {"range": f"'Missmatch Invoices'!A2:F{len(missmatches) + 2}", "values": missmatches},
        ])


  def update(self) -> dict:
    self.output_vendor_info()
    self.new_referrals_acceptance()
    self.output_appointment_info()
    self.output_missmatch_invoices()
    return self.dd4_service.update(
        'reports',{'id': self.spreadsheet_id, 'title': self.title},
        ['title'])


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

    entities = []
    if type == 'patients':
      entities = self.dd4_service.list_for_report(
        'patients',['status!=Discharged'], page_size=2750)['items']
      entities.extend(self.dd4_service.list_for_report(
        'patients',['status=Discharged'], page_size=2750)['items'])
    elif type == 'vendors':
      entities = self.dd4_service.list('vendors', page_size=2750)['items']
    elif type == 'invoices':
      s = int(datetime(year, 1, 1, 0, 0, 0).timestamp() * 1000)
      e = int(datetime(year + 1, 1, 1, 0, 0, 0).timestamp() * 1000)
      entities = self.dd4_service.list('invoices', [f'date%3E{s},date%3C{e}'], page_size=2750)['items']
    elif type == 'appointments' or type == 'patient_histories':
      for m in range(1, 13):
        s = int(datetime(year, m, 1, 0, 0, 0).timestamp() * 1000)
        e = int(datetime(year if m < 12 else year + 1, m + 1 if m < 12 else 1, 1, 0, 0, 0).timestamp() * 1000)
        if type == 'appointments':
          entities.extend(self.dd4_service.list_for_report(
            'appointments', [f'start%3E{s},start%3C{e}'], page_size=2750)['items'])
        else:
          entities.extend(self.dd4_service.list_for_report(
              'changeHistorys',
              ['entityType=Patient','action=UPDATED',f'timeStamp%3E{s},timeStamp%3C{e}'],
               'timeStamp', 2750)['items'])
    else:
      raise ValueError(f'Unknown type: {type}')

    if self.use_file_io:
      with open(file_path, "w", encoding="utf-8") as f:
        for e in entities:
          json.dump(e, f)
          f.write("\n")

    return entities


if __name__ == "__main__":
  with open('data/dd4_token-test.txt', 'r') as f:
    id_token = f.readline()

  # sheets_api.create('Financial & Referral KPI 2024')
  # sheets_api.copy_file(spreadsheet_id, 'Financial & Referral KPI 2025')
  report_updater = ReportUpdater(SPREADSHEET_2024_TEST, id_token, True)
  print(report_updater.update())
