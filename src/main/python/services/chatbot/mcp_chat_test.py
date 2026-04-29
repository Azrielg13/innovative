import json
from dotenv import load_dotenv
load_dotenv()
from mcp_chat import MCPAgent

with open('../mcp/token.id', 'r') as f:
  id_token = f.readline()
  agent = MCPAgent(system=f"idToken={id_token}")

def query(question):
  return agent(question)['text']


def test_fetch_appointment():
  result = query("Fetch appointment 6203642319208448")
  assert "6203642319208448" in result
  assert "Decan" in result
  assert "St John" in result
  assert len(agent.messages) == 2
  assert "Fetch appointment 6203642319208448" in agent.messages[0]['content']
  assert "Decan" in agent.messages[1]['content']


def test_search_patient():
  assert "St John" in query("What patients are there named Decan?")
  assert len(agent.messages) == 4
  assert "Fetch appointment 6203642319208448" in agent.messages[0]['content']
  assert "Decan" in agent.messages[1]['content']
  assert "What patients are there named Decan?" in agent.messages[2]['content']
  assert "St John" in agent.messages[3]['content']


def test_search_nurse():
  assert "Dr Greg" in query("What nurses do we have that live in Corona?")
  assert len(agent.messages) == 6


def test_fetch_appointments():
  result = query("How many appointments did we have between Feb 12th and Feb 16th 2026?")
  assert "2 appointments" in result
  assert len(agent.messages) == 8

  result = query("How many of those appointments have the billing status of Billable and Payable?")
  assert "1 appointment" in result
  assert len(agent.messages) == 10


def test_search_nurse_two_words():
  assert "Corona" in query("What city does Levi Mackabee live in?")
  assert len(agent.messages) == 12
