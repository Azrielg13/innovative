from dotenv import load_dotenv
from sms import login, detect_sms_and_send

load_dotenv()


def test_login():
  assert login() is not None


def test_detect_sms_and_send():
  try:
    detect_sms_and_send(
        login(),
        [{"phoneNumber": "909-800-0300"}],
        "Hello From IP360!") is not None
  except Exception as e:
    assert "None of this user's phone number(s)" in str(e)
