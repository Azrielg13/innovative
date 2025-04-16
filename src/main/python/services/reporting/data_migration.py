import dd4_service
import json
from multiprocessing import Pool


def get_histories(patient):
  histories = dd4_service.list('changeHistorys', {
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

  response = dd4_service.list('appointments', {
    'filter': f'patientId={patient["id"]}', 'orderBy': 'start', 'pageSize': 1})

  # If the patient has never had an appointment, exit.
  if response['totalSize'] == 0:
    return patient

  first_appointment = response['items'][0]
  patient['firstAppointmentId'] = first_appointment['id']
  patient['firstAppointmentDate'] = first_appointment['date']

  if patient.get('creationTime') is None or patient.get('creationTime') > first_appointment['date']:
    patient['creationTime'] = first_appointment['date']

  return dd4_service.update(
      'patients', patient,
      ['creationTime', 'firstAppointmentId', 'firstAppointmentDate'])


def migrate_creation_time(patient):
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


def migrate_data():
  patients = dd4_service.list('patients', {'pageSize': 2750})['items']

  with Pool() as pool:
    patients = pool.map(migrate_creation_time, patients)

  # with Pool() as pool:
  patients = list(map(migrate_first_appointment, patients))

  print(f'patients: {len(patients)}')