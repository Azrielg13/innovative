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


def read_config(spreadsheet_id):
  spreadsheet = sheets_api.get(spreadsheet_id)
  title = spreadsheet['properties']['title']
  pattern = r'^(.*)\s(\d{4})\s*(\(test\))?$'
  match = re.match(pattern, title)
  if not match:
    return None  # Or raise an appropriate exception based on requirements
  type = match.group(1).strip()
  year = int(match.group(2))
  test_indicator = bool(match.group(3))
  return {
    'title': title,
    'type': type,
    'year': year,
    'test': test_indicator
  }


def to_patient_data_row(patient, year):
  id = patient['id']
  creation = int(patient.get('creationTime') or 0)
  creation_datetime = datetime.fromtimestamp(creation / 1000)

  rt = int(patient.get('referralResolutionDate') or 0)

  month = MONTH_NAMES[creation_datetime.month - 1]
  if creation_datetime.year != year:
    month = f'{creation_datetime.year}-{month}'

  return [id, creation, month, patient.get('referralResolution'), rt,
          round((rt - creation) / ONE_MINUTE,1) if rt > 0 else '',
          patient.get('billingVendorName') or '', patient['condition']]
  # return [id, creation, month, 'Pending', None, None, patient.get('billingVendorName')]


def new_referrals_acceptance(spreadsheet_id, year, cached_reader):
  patients = cached_reader.get_data('patients', year)

  vendors = {}
  resolution_infos = []
  for patient in patients:
    resolution_info = to_patient_data_row(patient, year)
    # print(resolution_info)
    if resolution_info is None:
      continue
    resolution_infos.append(resolution_info)
    if resolution_info[6] is not None:
      vendors[resolution_info[6]] = [resolution_info[6]]

  print(resolution_infos)
  sheets_api.batch_update_values(
      spreadsheet_id,
      "USER_ENTERED",
      [
        {"range": "'Referrals Data'!A1:H1", "values": [['Patient Id', 'Referral Ts', 'Month', 'Resolution', 'Resolution Ts', 'Processing Time (Mins)', 'Client', 'Referral Type']]},
        {"range": f"'Referrals Data'!A2:H{len(resolution_infos) + 2}", "values": resolution_infos},
      ])


def to_appointment_row(appointment):
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


def output_appointment_info(spreadsheet_id, year, cached_reader):
  patients = {p['id']: p for p in cached_reader.get_data('patients', year)}

  appointments = cached_reader.get_data('appointments', year)

  appointments = list(
      map(lambda a:{**a, 'patient':patients.get(a['patientId'])}, appointments))

  appointment_rows = list(map(to_appointment_row, appointments))

  sheets_api.batch_update_values(
      spreadsheet_id,
      "USER_ENTERED",
      [
        {"range": "'Appointment Data'!A1:K1", "values": [
          ['Appointment Id', 'Patient Id', 'Date', 'Month', 'Client', 'Hours',
           'New Hours', 'Billing Total', 'Payment Total']]},
        {"range": f"'Appointment Data'!A2:K{len(appointment_rows) + 2}", "values": appointment_rows},
      ])


def output_vendor_info(spreadsheet_id, year, cached_reader):
  vendors = cached_reader.get_data('vendors', year)

  vendor_names = list(sorted(map(lambda v:v['name'], vendors)))
  vendor_names = [[name] for name in vendor_names]

  print(vendor_names)
  sheets_api.batch_update_values(
      spreadsheet_id,
      "USER_ENTERED",
      [
        {"range": f"'Referrals by Client'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
        {"range": f"'# of Visits'!A5:B{len(vendor_names) + 5}", "values": vendor_names},
      ])


class CachedReader:
  def __init__(self, dd4_service, use_file_io=False):
    self.dd4_service = dd4_service
    self.use_file_io = use_file_io
    self.cached_data = {}

  def get_data(self, type, year):
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


def update_spreadsheet(spreadsheet_id, id_token, use_cache_file=False):
  config = read_config(spreadsheet_id)
  # sheets_api.add_sheet(spreadsheet_id, 'Referrals Data')
  # sheets_api.add_sheet(spreadsheet_id, 'Referrals by Client')
  # sheets_api.add_sheet(spreadsheet_id, 'Appointment Data')
  # migrate_data()
  dd4_service = DD4Service(id_token, config.get('test'))
  cached_reader = CachedReader(
      dd4_service, use_cache_file and config['year'] < 2025)
  output_vendor_info(spreadsheet_id, config['year'], cached_reader)
  new_referrals_acceptance(spreadsheet_id, config['year'], cached_reader)
  output_appointment_info(spreadsheet_id, config['year'], cached_reader)
  return dd4_service.update(
      'reports',{'id': spreadsheet_id, 'title': config['title']},
      ['title'])


if __name__ == "__main__":
  with open('dd4_token-test.txt', 'r') as f:
    id_token = f.readline()

  # sheets_api.create('Financial & Referral KPI 2024')
  # sheets_api.copy_file(spreadsheet_id, 'Financial & Referral KPI 2025')
  report = update_spreadsheet(SPREADSHEET_2024_TEST, id_token, True)
  print(report)
