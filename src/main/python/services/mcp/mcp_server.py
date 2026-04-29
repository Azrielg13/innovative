import json
from fastmcp import FastMCP
from urllib import parse, request

mcp = FastMCP("IP360 MCP Server 🧑🏾‍⚕️")

API_BASE = "https://test-dot-ip360-179401.uc.r.appspot.com/_api/{}/v1/{}?idToken={}"


def send_json_req(entity, action, id_token, params=None, data=None):
  url = API_BASE.format(entity, action, id_token)
  if params is not None:
    url += "&" + parse.urlencode(params)
  req = request.Request(url)

  json_data = None
  if data is not None:
    json_data = json.dumps(data).encode('utf-8')
    req.add_header('Content-Type', 'application/json')
    req.add_header('Content-Length', str(len(json_data)))

  print(f'Sending request: {url}' + '' if json_data is None else f'with data: {json_data}')
  with request.urlopen(req, json_data) as resp:
    response = json.load(resp)
    print('Response: ', response)
    return response


@mcp.resource("appointment://{id}/fetch?idToken={id_token}")
def fetch_appointment_resource(id:int, id_token:int) -> str:
  """Fetches an Appointment
    Args:
      id Unique identifier of the appointment to fetch as long number: 458186112
      id_token The session token of the user.
    Returns:
       json object of the requested appointment.
    Examples:
      appointments://6203642319208448/fetch?id_token=123
      returns {
        "id": "6203642319208448",
        "creationTime": "1770822806239",
        "lastModifiedTime": "1770822806239",
        "creationUsername": "eddiemay",
        "lastModifiedUsername": "eddiemay",
        "patientId": "6227693880213504",
        "patientName": "Decan St John",
        "nurseId": "6228541880401920",
        "nurseName": "Dr Levi Mackabee",
        "vendorId": "6262417818386432",
        "vendorName": "Doctors Hospital",
        "date": "1770969600000",
        "cancelled": false,
        "state": "PENDING_ASSESSMENT",
        "assessmentComplete": false,
        "assessmentApproved": false,
        "loggedHours": 0,
        "mileage": 0,
        "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
        },
        "start": "1770969600000",
        "end": "1770969600000",
        "assPercentComplete": 0
      }
  """
  return send_json_req('appointments', 'get', id_token, {'id': id})


@mcp.tool
def fetch_appointment(id:int, id_token:str) -> str:
  """Fetches an Appointment
    Args:
      id Unique identifier of the appointment to fetch as long number: 458186112
      id_token The session token of the user.
    Returns:
       json object of the requested appointment.
    Examples:
      fetch_appointment(6203642319208448, 123)
      returns {
        "id": "6203642319208448",
        "creationTime": "1770822806239",
        "lastModifiedTime": "1770822806239",
        "creationUsername": "eddiemay",
        "lastModifiedUsername": "eddiemay",
        "patientId": "6227693880213504",
        "patientName": "Decan St John",
        "nurseId": "6228541880401920",
        "nurseName": "Dr Levi Mackabee",
        "vendorId": "6262417818386432",
        "vendorName": "Doctors Hospital",
        "date": "1770969600000",
        "cancelled": false,
        "state": "PENDING_ASSESSMENT",
        "assessmentComplete": false,
        "assessmentApproved": false,
        "loggedHours": 0,
        "mileage": 0,
        "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
        },
        "start": "1770969600000",
        "end": "1770969600000",
        "assPercentComplete": 0
      }
  """
  return send_json_req('appointments', 'get', id_token, {'id': id})


@mcp.tool
def fetch_appointments(start_date:int, end_date:int, id_token:str, state:str=None) -> str:
  """ Fetches an Appointments for a date range from the datastore.
    Args:
      start_date The start date of the date range.
      end_date The end date of the date range.
      id_token The session token of the user.
      state Optional filter state of the appointment to filter to.
        Options are: UNCONFIRMED, CONFIRMED, CANCELLED, DELETED, PENDING_ASSESSMENT,
        PENDING_APPROVAL, BILLABLE_AND_PAYABLE, BILLABLE, PAYABLE, CLOSED.
    Returns:
       json array of the matching appointments.
    Examples:
      fetch_appointments(1770883200000, 1771228800000, 123)
      returns {
       "type": "Appointment",
       "items": [
        {
         "id": "6246695071383552",
         "creationTime": "1770773629202",
         "lastModifiedTime": "1777403191078",
         "creationUsername": "eddiemay",
         "lastModifiedUsername": "eddiemay",
         "patientId": "6262144785973248",
         "patientName": "Eddie Mayfield",
         "nurseId": "6228541880401920",
         "nurseName": "Dr Levi Mackabee",
         "vendorId": "6262417818386432",
         "vendorName": "Doctors Hospital",
         "date": "1770883200000",
         "cancelled": false,
         "state": "BILLABLE_AND_PAYABLE",
         "assessmentComplete": true,
         "assessmentApproved": true,
         "timeIn": "284069100000",
         "timeOut": "284076000000",
         "loggedHours": 2.0,
         "mileage": 20.0,
         "fromZipCode": "92860",
         "toZipCode": "92335",
         "paymentInfo": {
          "serviceCode": "Infusion Visit 11",
          "unit": "Visit",
          "unitCount": 1.0,
          "unitRate": 110.0,
          "mileage": 20.0,
          "mileageRate": 0.5,
          "subTotal": 110.0,
          "mileageTotal": 10.0,
          "total": 120.0
         },
         "billingInfo": {
          "serviceCode": "6262417818386432-Per Visit",
          "unit": "Visit",
          "unitCount": 1.0,
          "unitRate": 350.0,
          "mileage": 20.0,
          "mileageRate": 0.55,
          "subTotal": 350.0,
          "mileageTotal": 11.0,
          "total": 361.0
         },
         "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
         },
         "assPercentComplete": 0.0,
         "start": "1770883200000",
         "end": "1770883200000"
        },
        {
         "id": "6203642319208448",
         "creationTime": "1770822806239",
         "lastModifiedTime": "1770822806239",
         "creationUsername": "eddiemay",
         "lastModifiedUsername": "eddiemay",
         "patientId": "6227693880213504",
         "patientName": "Decan St John",
         "nurseId": "6228541880401920",
         "nurseName": "Dr Levi Mackabee",
         "vendorId": "6262417818386432",
         "vendorName": "Doctors Hospital",
         "date": "1770969600000",
         "cancelled": false,
         "state": "PENDING_ASSESSMENT",
         "assessmentComplete": false,
         "assessmentApproved": false,
         "loggedHours": 0.0,
         "mileage": 0.0,
         "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
         },
         "assPercentComplete": 0.0,
         "start": "1770969600000",
         "end": "1770969600000"
        }
       ],
       "totalSize": 2,
       "orderBy": "date",
       "pageSize": 50,
       "pageToken": 1,
       "filter": "start>=1770883200000,start<=1771228800000"
      }
      fetch_appointments(1770883200000, 1771228800000, 123. 'BILLABLE_AND_PAYABLE')
      returns {
       "type": "Appointment",
       "items": [
        {
         "id": "6246695071383552",
         "creationTime": "1770773629202",
         "lastModifiedTime": "1777403191078",
         "creationUsername": "eddiemay",
         "lastModifiedUsername": "eddiemay",
         "patientId": "6262144785973248",
         "patientName": "Eddie Mayfield",
         "nurseId": "6228541880401920",
         "nurseName": "Dr Levi Mackabee",
         "vendorId": "6262417818386432",
         "vendorName": "Doctors Hospital",
         "date": "1770883200000",
         "cancelled": false,
         "state": "BILLABLE_AND_PAYABLE",
         "assessmentComplete": true,
         "assessmentApproved": true,
         "timeIn": "284069100000",
         "timeOut": "284076000000",
         "loggedHours": 2.0,
         "mileage": 20.0,
         "fromZipCode": "92860",
         "toZipCode": "92335",
         "paymentInfo": {
          "serviceCode": "Infusion Visit 11",
          "unit": "Visit",
          "unitCount": 1.0,
          "unitRate": 110.0,
          "mileage": 20.0,
          "mileageRate": 0.5,
          "subTotal": 110.0,
          "mileageTotal": 10.0,
          "total": 120.0
         },
         "billingInfo": {
          "serviceCode": "6262417818386432-Per Visit",
          "unit": "Visit",
          "unitCount": 1.0,
          "unitRate": 350.0,
          "mileage": 20.0,
          "mileageRate": 0.55,
          "subTotal": 350.0,
          "mileageTotal": 11.0,
          "total": 361.0
         },
         "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
         },
         "assPercentComplete": 0.0,
         "start": "1770883200000",
         "end": "1770883200000"
        },
       ],
       "totalSize": 1,
       "orderBy": "date",
       "pageSize": 50,
       "pageToken": 1,
       "filter": "start>=1770883200000,start<=1771228800000"
      }
  """
  filters = f"start>={start_date},start<={end_date}" + ('' if state is None or len(state) == 0 else ',state=' + state)
  return send_json_req('appointments', 'list', id_token, {'filter': filters})


@mcp.tool
def search(entity_type:str, search_text:str, id_token:str) -> str:
  """Searches an entity type for
    Args:
      entity_type The entity type to search, make sure the name ends in a s.
        Searchable entity include employees, invoices, nurses, patients and vendors.
      search_text The text to use for the text. This could be a name.
      id_token The session token used for the user to access the api.
    Returns:
       A json array of the values that match the search.
    Examples:
      search('patients', 'John', 123)
      returns {
       "type": "Patient",
       "items": [
        {
         "id": "6227693880213504",
         "referralDate": "1719817200000",
         "creationTime": "1719730800000",
         "referralSource": "Doctors Hospital",
         "billingVendorName": "Doctors Hospital",
         "firstName": "Decan",
         "lastName": "St John",
         "dateOfBirth": "646815600000",
         "diagnosis": "Primary Immune Deficiencies",
         "therapyType": "Remicade",
         "ivAccess": "Port",
         "startOfCareDate": "1719817200000",
         "serviceAddress": {
          "address": "212 W Mission Ct, Corona, CA 92882, USA",
          "latitude": 33.8603344,
          "longitude": -117.570817
         },
         "phonePrimary": {
          "number": "951-123-4567"
         },
         "phoneAlternate": {
          "number": "951-765-4321"
         },
         "emergencyContact": "Sarah Irene Witacure",
         "emergencyContactPhone": {
          "number": "213-854-2611"
         },
         "rx": "Rimdes",
         "estLastDayOfService": "1733040000000",
         "labs": false,
         "labsFrequency": "Weekly",
         "infoInSOS": false,
         "status": "Active",
         "referralResolutionDate": "1719860636945",
         "referralResolution": "Accepted",
         "condition": "Unspecified",
         "firstAppointmentDate": "1719730800000",
         "toString": "Decan St John",
         "fullName": "Decan St John"
        }
       ],
       "totalSize": 1,
       "pageSize": 50,
       "pageToken": 1,
       "orderBy": ""
      }
  """
  return send_json_req(entity_type, 'search', id_token, {'searchText': search_text})


@mcp.tool
def create_appointment(appointment:dict, id_token) -> str:
  """Creates a new appointment in the datastore. When
    Args:
      appointment The appointment to create.
        When creating an appointment we want to make at the very least we have
        the patient_id, nurse_id and the date of the appointment.
    Returns:
       A json object of the appointment that was created.
    Examples:
      create_appointment({'patientId': 6227693880213504, 'nurseId': 6228541880401920, 'date': 1770969600000, 'titaration': '2hr'})
      returns {
        "id": "6203642319208448",
        "creationTime": "1770822806239",
        "lastModifiedTime": "1770822806239",
        "creationUsername": "eddiemay",
        "lastModifiedUsername": "eddiemay",
        "patientId": "6227693880213504",
        "patientName": "Decan St John",
        "nurseId": "6228541880401920",
        "nurseName": "Dr Levi Mackabee",
        "vendorId": "6262417818386432",
        "vendorName": "Doctors Hospital",
        "date": "1770969600000",
        "cancelled": false,
        "state": "UNCONFIRMED",
        "assessmentComplete": false,
        "assessmentApproved": false,
        "loggedHours": 0,
        "mileage": 0,
        "repeat": {
          "type": "Does_not_repeat",
          "toString": "Does not repeat"
        },
        "start": "1770969600000",
        "end": "1770969600000",
        "assPercentComplete": 0
      }
  """
  return send_json_req('appointments', 'create', id_token, data=appointment)


if __name__ == "__main__":
  mcp.run(transport='stdio')