import json
from multiprocessing import Pool
from dd4_service import DD4Service


class Connector:
  def __init__(self, dd4_service=None):
    self.dd4_service = dd4_service

connector = Connector()


def get_histories(patient):
  histories = connector.dd4_service.list('changeHistorys', {
    'filter': f'entityType%3DPatient,entityId%3D{patient["id"]}',
    'orderBy': 'timeStamp'})['items']
  patient['histories'] = histories

  patient['creates'] = [h for h in histories if h['action'] == 'CREATED']

  return histories


def get_resolution_info(histories):
  for history in histories[1:]:
    entity = json.loads(history['entity'])
    # If this changed status via migration then we don't want it.
    if history['action'] != 'UPDATED' and entity.get('status') is not None and entity['status'] != 'Pending':
      return None, None
    # Must have changed status during a update to be good.
    elif history['action'] == 'UPDATED' and entity.get('status') is not None and entity['status'] != 'Pending':
      rt = int(history['timeStamp'])
      if entity['status'] == 'Denied' or entity['status'] == 'Declined':
        return 'Declined', rt
      elif entity['status'] == 'Cancelled':
        return 'Cancelled', rt
      else:
        return 'Accepted', rt

  return None, None


def migrate_first_appointment(patient):
  if patient.get('firstAppointmentDate') is not None:
    return patient

  response = connector.dd4_service.list('appointments', {
    'filter': f'patientId={patient["id"]}', 'orderBy': 'start', 'pageSize': 1})

  # If the patient has never had an appointment, exit.
  if response['totalSize'] == 0:
    return patient

  first_appointment = response['items'][0]
  patient['firstAppointmentId'] = first_appointment['id']
  patient['firstAppointmentDate'] = first_appointment['date']

  if patient.get('creationTime') is None or patient.get('creationTime') > first_appointment['date']:
    patient['creationTime'] = first_appointment['date']

  return connector.dd4_service.update(
      'patients', patient,
      ['creationTime', 'firstAppointmentId', 'firstAppointmentDate'])


def migrate_creation_time(dd4_service, patient):
  if patient.get('creationTime') is not None:
    return patient

  histories = get_histories(patient)
  patient['creationTime'] = histories[0]['timeStamp']
  patient['referralResolution'], patient['referralResolutionDate'] =(
    get_resolution_info(histories))
  return dd4_service.update(
      'patients', patient,
      ['creationTime', 'referralResolution', 'referralResolutionDate']
      if patient['referralResolution'] is not None else ['creationTime'])


def migrate_data(is_test):
  with open('dd4_token-test.txt' if is_test else 'dd4_token.txt', 'r') as f:
    dd4_service = DD4Service(f.readline(), is_test)
    connector.dd4_service = dd4_service

  patients = dd4_service.list('patients', pageSize=2750)['items']

  with Pool() as pool:
    patients = pool.map(migrate_creation_time, patients)

  # with Pool() as pool:
  patients = list(map(migrate_first_appointment, patients))

  print(f'patients: {len(patients)}')


def copy_vendors():
  with open('data/dd4_token-test.txt', 'r') as f:
    test_service = DD4Service(f.readline(), True)

  with open('data/2024-vendors.json', "r", encoding="utf-8") as f:
    vendors = list(map(json.loads, f))

  for vendor in vendors:
    test_service.create('vendors', vendor)


def copy_license_generaldata():
  with open('data/dd4_token.txt', 'r') as f:
    prod_service = DD4Service(f.readline(), False)
  with open('data/dd4_token-test.txt', 'r') as f:
    test_service = DD4Service(f.readline(), True)

  license_cats = prod_service.list("generalDatas", filters={"groupId=889"})['items']
  licenses = []
  for license_cat in license_cats:
    licenses.extend(prod_service.list("generalDatas", filters={f"groupId={license_cat['id']}"})['items'])

  test_service.create('generalDatas', prod_service.get("generalDatas", 889))
  for license_cat in license_cats:
    test_service.create('generalDatas', license_cat)
  for license in licenses:
    test_service.create('generalDatas', license)


if __name__ == "__main__":
  copy_license_generaldata()