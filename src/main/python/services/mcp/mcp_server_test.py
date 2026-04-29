import asyncio
import json
import mcp_server
from fastmcp import Client
from urllib import parse

client = Client(mcp_server.mcp)
session = {}


def get_id_token():
  if session.get('id') is None:
    with open('token.id', 'r') as f:
      session['id'] = f.readline()

  return session['id']


async def call_tool(name: str, arguments: dict):
  print(f"Testing {name} with arguments {dict}")
  async with client:
    result = await client.call_tool(name, arguments)
    print(result)
    return result


async def read_resource(url: str):
  async with client:
    result = await client.read_resource(url)
    print(result)
    return result


async def get_prompt(name: str, arguments: dict):
  async with client:
    result = await client.get_prompt(name, arguments)
    print(result)
    return result


def test_fetch_appointment_resource():
  appointment = json.loads(asyncio.run(read_resource(
      f"appointment://6203642319208448/fetch?idToken={get_id_token()}"))[0].text)

  assert appointment['id'] == '6203642319208448'
  assert appointment['patientId'] == '6227693880213504'
  assert appointment['nurseId'] == '6228541880401920'
  assert appointment['vendorId'] == '6262417818386432'
  assert appointment['date'] == '1770969600000'


def test_fetch_appointment():
  appointment = json.loads(asyncio.run(call_tool(
      "fetch_appointment",
      {"id": 6203642319208448, "id_token": get_id_token()}))[0].text)

  assert appointment['id'] == '6203642319208448'
  assert appointment['patientId'] == '6227693880213504'
  assert appointment['nurseId'] == '6228541880401920'
  assert appointment['vendorId'] == '6262417818386432'
  assert appointment['date'] == '1770969600000'


def test_fetch_appointments():
  appointments = json.loads(asyncio.run(call_tool(
      "fetch_appointments",
      {"start_date": 1770883200000, "end_date": 1771228800000,"id_token": get_id_token()}))[0].text)

  assert len(appointments['items']) == 2
  appointment = appointments['items'][0]
  assert appointment['id'] == '6246695071383552'
  assert appointment['patientId'] == '6262144785973248'
  assert appointment['nurseId'] == '6228541880401920'
  assert appointment['vendorId'] == '6262417818386432'
  assert appointment['date'] == '1770883200000'

  appointment = appointments['items'][1]
  assert appointment['id'] == '6203642319208448'
  assert appointment['patientId'] == '6227693880213504'
  assert appointment['nurseId'] == '6228541880401920'
  assert appointment['vendorId'] == '6262417818386432'
  assert appointment['date'] == '1770969600000'


def test_fetch_appointments_with_state():
  appointments = json.loads(asyncio.run(call_tool(
      "fetch_appointments",
      {"start_date": 1770883200000, "end_date": 1771228800000, "state": 'BILLABLE_AND_PAYABLE', "id_token": get_id_token()}))[0].text)


  assert len(appointments['items']) == 1
  appointment = appointments['items'][0]
  assert appointment['id'] == '6246695071383552'
  assert appointment['state'] == 'BILLABLE_AND_PAYABLE'
  assert appointment['patientId'] == '6262144785973248'
  assert appointment['nurseId'] == '6228541880401920'
  assert appointment['vendorId'] == '6262417818386432'
  assert appointment['date'] == '1770883200000'


def test_search_patient():
  search_result = json.loads(asyncio.run(call_tool(
      "search",
      {"entity_type": "patients", "search_text": "John", "id_token": get_id_token()}))[0].text)

  assert len(search_result['items']) == 1
  patient = search_result['items'][0]
  assert patient['id'] == '6227693880213504'
  assert patient['firstName'] == 'Decan'
  assert patient['lastName'] == 'St John'
  assert patient['dateOfBirth'] == "646815600000"
  assert patient['rx'] == "Rimdes"


def test_search_2words():
  search_result = json.loads(asyncio.run(call_tool(
      "search",
      {"entity_type": "nurses", "search_text": "Levi Mackabee", "id_token": get_id_token()}))[0].text)

  assert len(search_result['items']) == 1
  nurse = search_result['items'][0]
  assert nurse['id'] == '6228541880401920'
  assert nurse['firstName'] == 'Dr Levi'
  assert nurse['lastName'] == 'Mackabee'
