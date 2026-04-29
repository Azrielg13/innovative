import main
from dotenv import load_dotenv

load_dotenv()


def test_root():
  main.app.testing = True
  client = main.app.test_client()

  r = client.get("/")
  assert r.status_code == 501
  assert "Bad Request" in r.data.decode("utf-8")


def test_send_sms():
  main.app.testing = True
  client = main.app.test_client()

  payload = {
    "recipients": [{"phone_number": "909-800-0300"}],
    "message": "Hello from IP360!"}

  r = client.post("/sendSMS", json=payload) # Flask auto sets Content-Type: application/json

  assert r.status_code == 500
  assert "None of this user's phone number(s)" in r.data.decode("utf-8")

