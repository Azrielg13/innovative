import os, sys, time
import traceback
from ringcentral import SDK

# Read phone number(s) that belongs to the authenticated user and detect if a phone number
# has the SMS capability
def detect_sms_and_send(platform, recipients, text):
  resp = platform.get("/restapi/v1.0/account/~/extension/~/phone-number")
  jsonObj = resp.json()
  for record in jsonObj.records:
    print(record.__dict__)
    for feature in record.features:
      if feature == "SmsSender":
        # If user has multiple phone numbers, check and decide which number
        # to be used for sending SMS message. For simplicity, we pick the first one we find.
        return send_sms(platform, record.phoneNumber, recipients, text)
  if len(jsonObj.records) == 0:
    raise Exception("This user does not own a phone number!")
  else:
    raise Exception("None of this user's phone number(s) has the SMS capability!")

# Send a text message from a user own phone number to a recipient number
def send_sms(platform, fromNumber, recipients, text):
  bodyParams = {
    'from' : { 'phoneNumber': fromNumber },
    'to'   : recipients, # Array (max 10 recipients)
    'text' : text
  }
  endpoint = "/restapi/v1.0/account/~/extension/~/sms"
  resp = platform.post(endpoint, bodyParams)
  jsonObj = resp.json()
  msg = "SMS sent. Message id: " + str(jsonObj.id)
  print (msg)
  check_message_status(jsonObj.id)
  return msg

# Check the sending message status until it's out of the queued status
def check_message_status(platform, messageId):
  endpoint = "/restapi/v1.0/account/~/extension/~/message-store/" + str(messageId)
  resp = platform.get(endpoint)
  jsonObj = resp.json()
  print ("Message status: " + jsonObj.messageStatus)
  if (jsonObj.messageStatus == "Queued"):
    time.sleep(2)
    check_message_status(jsonObj.id)

# Authenticate a user using a personal JWT token
def login():
  try:
    # Instantiate the SDK and get the platform instance
    rcsdk = SDK(os.environ.get('RC_APP_CLIENT_ID'),
                os.environ.get('RC_APP_CLIENT_SECRET'),
                os.environ.get('RC_SERVER_URL'))
    platform = rcsdk.platform()
    platform.login(jwt=os.environ.get('RC_USER_JWT'))
    return platform
  except Exception as e:
    traceback.print_exc()
    raise("Unable to authenticate to platform. Check credentials." + str(e))

if __name__ == "__main__":
  from dotenv import load_dotenv
  load_dotenv()
  platform = login()
  detect_sms_and_send(platform, [{'phoneNumber': '909-800-0300'}], "Hello From IP360!")