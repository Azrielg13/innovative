import json
from dotenv import load_dotenv
load_dotenv()
from mcp_chat import MCPAgent

with open('../mcp/token.id', 'r') as f:
  id_token = f.readline()
  agent = MCPAgent(system=f"idToken={id_token}")


def query(question):
  return agent(question)['text']


def test_list_types():
  result = query("What types are there in the datastore?")
  assert 'Appointment' in result
  assert 'Nurse' in result
  assert 'Invoice' in result
  assert len(agent.messages) == 2


def test_describe():
  assert 'timeZone' in query("What fields does the Nurse object have")
  assert len(agent.messages) == 4


def test_fetch_appointment():
  result = query("Fetch appointment 6203642319208448")
  assert "6203642319208448" in result
  assert "Decan" in result
  assert "St John" in result
  assert len(agent.messages) == 6


def test_search_patient():
  assert "St John" in query("What patients are there named Decan?")
  assert len(agent.messages) == 8


def test_search_nurse():
  assert "Dr Greg" in query("Search nurses that live in Corona?")
  assert len(agent.messages) == 10


def test_fetch_appointments():
  result = query("How many appointments did we have between Feb 12th and Feb 16th 2026?")
  assert "2 appointments" in result
  assert len(agent.messages) == 12


def test_appointments_followup():
  result = query("How many of those appointments have the billing status of Billable and Payable?")
  assert "1 appointment" in result
  assert len(agent.messages) == 14


def test_search_nurse_two_words():
  assert "Corona" in query("What city does Levi Mackabee live in?")
  assert len(agent.messages) == 16


def test_create_note():
  assert "Note created" in query("Create a note for Dr Greg stating he will be out of office for 3 weeks.")
  assert len(agent.messages) == 18


def test_update_note():
  assert "Note updated" in query("Update note id 5452716612517888 to say 'This note has been updated at: ' and put the current time.")
  assert len(agent.messages) == 20
