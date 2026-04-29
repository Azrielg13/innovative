from flask import Flask, request
from sms import detect_sms_and_send, login

# If `entrypoint` is not defined in app.yaml, App Engine will look for an app
# called `app` in `main.py`.
app = Flask(__name__)

# Set CORS headers for the main request
HEADERS = {"Access-Control-Allow-Origin": "*"}


def do_cors():
  # For more information about CORS and CORS preflight requests, see:
  # https://developer.mozilla.org/en-US/docs/Glossary/Preflight_request
  # Set CORS headers for the preflight request
  # Allows GET requests from any origin with the Content-Type
  # header and caches preflight response for an 3600s
  headers = {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,POST",
    "Access-Control-Allow-Headers": "Content-Type",
    "Access-Control-Max-Age": "3600",
  }

  return "", 204, headers

@app.route("/")
def root():
  return do_cors() if request.method == "OPTIONS" else "Bad Request", 501, HEADERS


@app.route("/hello")
def hello():
  return "Hello from Python", 200, HEADERS


@app.route("/sendSMS", methods=["POST", "OPTIONS"])
def sendSMS():
  if request.method == "OPTIONS":
    return do_cors()

  try:
    data = request.get_json()
    recipients = data.get('recipients')
    text = data.get('message')
    return detect_sms_and_send(login(), recipients, text), 200, HEADERS
  except Exception as e:
    error = {
      "error": {
        "code": 500,
        "message": str(e)
      }
    }
    return error, 500, HEADERS

