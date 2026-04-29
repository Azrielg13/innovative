import asyncio
import json

from fastmcp import Client
from urllib import parse

session = {}


def get_id_token():
  if session.get('id') is None:
    with open('token.id', 'r') as f:
      session['id'] = f.readline()

  return session['id']


async def interact_with_server():
  print("--- Creating Client ---")

  # Option 1: Connect to a server run via `python my_server.py` (uses stdio)
  # client = Client("mcp_server.py")

  # Option 2: Connect to a server run via `python mcp_server.py`
  # client =  # Use the correct URL/port

  # print(f"Client configured to connect to: {client.target}")

  try:
    async with Client("mcp_server.py") as client:
      print("--- Client Connected ---")
      appointment = json.loads((await client.call_tool("fetch_appointment", {"id": 6203642319208448, "id_token": get_id_token()}))[0].text)
      assert appointment['id'] == '6203642319208448'
      assert appointment['patientId'] == '6227693880213504'
      assert appointment['nurseId'] == '6228541880401920'
      assert appointment['vendorId'] == '6262417818386432'
      assert appointment['date'] == '1770969600000'

      appointments = json.loads((await client.call_tool("fetch_appointments", {"start_date": 1770883200000, "end_date": 1771228800000, "id_token": get_id_token()}))[0].text)
      assert len(appointments['items']) == 2

      appointment = json.loads((await client.read_resource(f"appointment://6203642319208448/fetch?idToken={get_id_token()}"))[0].text)
      print('appointment:', appointment)
      assert appointment['id'] == '6203642319208448'
      assert appointment['patientId'] == '6227693880213504'
      assert appointment['nurseId'] == '6228541880401920'
      assert appointment['vendorId'] == '6262417818386432'
      assert appointment['date'] == '1770969600000'

      search_result = json.loads((await client.call_tool("search", {'entity_type':'patients', "search_text": "John", "id_token": get_id_token()}))[0].text)
      assert len(search_result['items']) == 1
      patient = search_result['items'][0]
      assert patient['id'] == '6227693880213504'
      assert patient['firstName'] == 'Decan'
      assert patient['lastName'] == 'St John'
      assert patient['dateOfBirth'] == "646815600000"
      assert patient['rx'] == "Rimdes"

  # except Exception as e:
    # print(f"An error occurred: {e}")
  finally:
    print("--- Client Interaction Finished ---")

if __name__ == "__main__":
  asyncio.run(interact_with_server())