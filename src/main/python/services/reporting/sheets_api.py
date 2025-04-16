from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
from google.oauth2 import service_account

SCOPES = ['https://www.googleapis.com/auth/drive.file']

class CredsProvider:
  creds = None

  def get(self):
    if self.creds is None:
      # Authenticate with the service account
      self.creds = service_account.Credentials.from_service_account_file(
          'service_account_key.json',
          scopes=['https://www.googleapis.com/auth/spreadsheets'])

    return self.creds

cp = CredsProvider()


def create(title):
  """
  Creates the Sheet the user has access to.
  Load pre-authorized user credentials from the environment.
  TODO(developer) - See https://developers.google.com/identity
  for guides on implementing OAuth2 for the application.
  """
  try:
    service = build("sheets", "v4", credentials=cp.get())
    spreadsheet = {"properties": {"title": title}}
    spreadsheet = (
      service.spreadsheets()
      .create(body=spreadsheet, fields="spreadsheetId")
      .execute()
    )
    print(f"Spreadsheet ID: {(spreadsheet.get('spreadsheetId'))}")
    return spreadsheet.get("spreadsheetId")
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


def get(spreadsheet_id):
  """
  Creates the Sheet the user has access to.
  Load pre-authorized user credentials from the environment.
  TODO(developer) - See https://developers.google.com/identity
  for guides on implementing OAuth2 for the application.
  """
  try:
    service = build("sheets", "v4", credentials=cp.get())
    # spreadsheet = {"properties": {"title": title}}
    spreadsheet = service.spreadsheets().get(
        spreadsheetId=spreadsheet_id, fields="spreadsheetId,properties").execute()
    print(f"Spreadsheet ID: {(spreadsheet.get('spreadsheetId'))}")
    return spreadsheet
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


def copy_file(file_id, new_name):
  drive_service = build('drive', 'v3', credentials=cp.get())
  try:
    copied_file = drive_service.files().copy(
        fileId=file_id,
        body={'name': new_name}
    ).execute()
    print(f"Spreadsheet ID: {(copied_file.get('id'))}")
    return copied_file.get("id")
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


def add_sheet(spreadsheet_id, title):
  """
  Adds a new tab to the spreadsheet.
  """
  try:
    service = build("sheets", "v4", credentials=cp.get())

    body = {"requests": [{"addSheet": {"properties": {'title': title}}}]}
    result = (
      service.spreadsheets()
      .batchUpdate(spreadsheetId=spreadsheet_id, body=body)
      .execute()
    )
    # print(f"{(result.get('totalUpdatedCells'))} cells updated.")
    return result
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


def batch_get_values(spreadsheet_id, range_names):
  """
  Creates the batch_update the user has access to.
  Load pre-authorized user credentials from the environment.
  TODO(developer) - See https://developers.google.com/identity
  for guides on implementing OAuth2 for the application.
  """
  try:
    service = build("sheets", "v4", credentials=cp.get())
    result = (
      service.spreadsheets().values()
      .batchGet(spreadsheetId=spreadsheet_id, ranges=range_names)
      .execute()
    )
    ranges = result.get("valueRanges", [])
    print(f"{len(ranges)} ranges retrieved")
    return result
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


def batch_update_values(spreadsheet_id, value_input_option, data):
  """
  Creates the batch_update the user has access to.
  Load pre-authorized user credentials from the environment.
  TODO(developer) - See https://developers.google.com/identity
  for guides on implementing OAuth2 for the application.
  """
  # pylint: disable=maybe-no-member
  try:
    service = build("sheets", "v4", credentials=cp.get())

    body = {"valueInputOption": value_input_option, "data": data}
    result = (
      service.spreadsheets().values()
      .batchUpdate(spreadsheetId=spreadsheet_id, body=body)
      .execute()
    )
    print(f"{(result.get('totalUpdatedCells'))} cells updated.")
    return result
  except HttpError as error:
    print(f"An error occurred: {error}")
    return error


if __name__ == "__main__":
  spreadsheet_id = create("Sheets Api Testing")

  # Pass: spreadsheet_id, value_input_option and data
  batch_update_values(
      spreadsheet_id,
      "USER_ENTERED",
      [
        {"range": "A1:C2", "values": [["A1", "B1"], ["A2", "B2"]]},
        {"range": "C3:E4", "values": [["C3", "D3"], ["C4", "D4"]]}])
  # [END sheets_batch_update_values]

  data = batch_get_values(spreadsheet_id, ['A1:C2', 'C3:E4'])