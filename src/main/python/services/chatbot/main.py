from datetime import datetime, timezone
from flask import Flask, request, jsonify
from google.cloud import datastore
from mcp_chat import MCPAgent

# If `entrypoint` is not defined in app.yaml, App Engine will look for an app
# called `app` in `main.py`.
app = Flask(__name__)


def cors_enabled_function(request):
    # For more information about CORS and CORS preflight requests, see:
    # https://developer.mozilla.org/en-US/docs/Glossary/Preflight_request
    # Set CORS headers for the preflight request
    if request.method == "OPTIONS":
        # Allows GET requests from any origin with the Content-Type
        # header and caches preflight response for an 3600s
        headers = {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "GET",
          "Access-Control-Allow-Headers": "Content-Type",
          "Access-Control-Max-Age": "3600",
        }

        return "", 204, headers

    # Set CORS headers for the main request
    return None, None, {"Access-Control-Allow-Origin": "*"}


def get_datastore_client():
  return datastore.Client()


@app.route("/")
def chat():
    # Check if this is a cores request.
    message, code, headers = cors_enabled_function(request)
    if code is not None:
      return message, code, headers

    datastore_client = get_datastore_client()

    question = request.args.get('question')
    session_id = request.args.get('sessionId')
    id_token = request.args.get('idToken')

    session = resolve_login(datastore_client, id_token)
    if session is None:
      return jsonify({'error': 'Not Authenticated'}), 401, {"Access-Control-Allow-Origin": "*", 'Content-Type': 'application/json'}

    # 🔍 Get client IP (Cloud Run compatible)
    ip_address = (
        request.headers.get("X-Forwarded-For", "").split(",")[0]
        or request.remote_addr)

    print(f"session_id: {session_id} question: {question} ip: {ip_address}")

    agent = get_agent(datastore_client, session_id, ip_address, id_token)

    # 🧠 Get model answer
    answer = agent(question)

    # 🗃️ Save to Datastore
    save_agent(datastore_client, session_id, agent)

    return answer, 200, headers


def get_agent(datastore_client:datastore.Client, session_id:str, ip_address, id_token):
  entity = datastore_client.get(
      datastore_client.key("ChatSession", session_id))

  if entity:
    agent = MCPAgent.from_dict(entity)
    agent.system = f"idToken={id_token}"
    return agent
  else:
    return MCPAgent(system=f"idToken={id_token}", ip_address=ip_address)


def save_agent(datastore_client:datastore.Client, session_id:str, agent:MCPAgent):
  entity = datastore.Entity(
      key=datastore_client.key("ChatSession", session_id),
      exclude_from_indexes=("messages",))
  entity.update(agent.to_dict())
  datastore_client.put(entity)


def resolve_login(datastore_client, id_token):
  if id_token is None:
    return None

  key = datastore_client.key("Session", id_token)
  session = datastore_client.get(key = key)
  if session is None or session['state'] != 'ACTIVE':
    return None

  now = datetime.now(timezone.utc)
  if session['expTime'] < now:
    entity = datastore.Entity(key = key)
    session['endTime'], session['state'] = now, 'CLOSED'
    entity.update(session)
    datastore_client.put(entity)
    return None

  return session

