import json
from urllib.request import urlopen, Request

PROD_API_BASE = 'https://ip360-179401.appspot.com/_api/{}/v1/{}?idToken={}'
TEST_API_BASE = 'https://test-dot-ip360-179401.uc.r.appspot.com/_api/{}/v1/{}?idToken={}'
#'https://test-dot-ip360-179401.uc.r.appspot.com/_api/patients/v1/get?id=6262144785973248&idToken=1266987330'


class DD4Service:
  def __init__(self, id_token=None, is_test=True):
    self.id_token = id_token
    self.is_test = is_test
    self.api_base = TEST_API_BASE if is_test else PROD_API_BASE


  def send_request(self, req):
    url = req.get('url') or self.api_base.format(req['service'], req['action'], self.id_token)
    params = req.get('params')
    for p in params or {}:
      if params[p] is not None:
        url = url + f'&{p}={params[p]}'

    data = None if req.get('data') is None else json.dumps(req.get('data')).encode('utf-8')

    print(f'data: {data}')

    print('Sending request: ', url)
    with urlopen(Request(url=url, method=req['method'], headers={'Content-type': 'application/json'}), data=data) as conn:
      response = json.load(conn)
      print('Response: ', response)
      return response


  def create(self, type, entity):
    return self.send_request(
        {'action': 'create', 'method': 'POST', 'service': type, 'data': entity})


  def get(self, type, id):
    return self.send_request({'action': 'get', 'method': 'GET', 'service': type,
                              'params': {'id': id}})


  def batch_get(self, type, ids):
    return self.send_request({'action': 'batchGet', 'method': 'GET',
                              'service': type, 'params': {'ids': ','.join(ids)}})


  def bulk_get(self, type, ids):
    return self.send_request({'action': 'bulkGet', 'method': 'POST',
                              'service': type, 'data': {'items': ids}})


  def list(self, type, filters=None, order_by=None, page_size=None, page_token=None):
    if filters is None:
      filters = []
    params = {
      "filter": ','.join(filters),
      'orderBy': order_by,
      'pageSize': page_size,
      'pageToken': page_token
    }

    return process_pagination(self.send_request(
        {'action': 'list', 'method': 'GET', 'service': type, 'params': params}))


  def list_as_ids(self, type, filters=None, order_by=None, page_size=None,
      page_token=None, id_token=None):
    if filters is None:
      filters = []
    params = {
      "filter": ','.join(filters),
      'orderBy': order_by,
      'pageSize': page_size,
      'pageToken': page_token
    }

    return process_pagination(self.send_request(
        {'action': 'listAsIds', 'method': 'GET', 'service': type, 'params': params}))


  def list_for_report(self, type, filters=None, order_by=None, page_size=None,
      page_token=None):
    if filters is None:
      filters = []
    params = {
      "filter": ','.join(filters),
      'orderBy': order_by,
      'pageSize': page_size,
      'pageToken': page_token
    }

    return process_pagination(self.send_request(
        {'action': 'listForReport', 'method': 'GET', 'service': type, 'params': params}))


  def search(self, type, params):
    return process_pagination(self.send_request(
        {'action': 'search', 'method': 'GET', 'service': type, 'params': params}))


  def update(self, type, entity, props):
    updated = {}
    for p in props:
      updated[p] = entity[p]

    return self.send_request(
        {'action': 'update', 'method': 'PUT', 'service': type, 'data': updated,
         'params': {'id': entity['id'], 'updateMask': ','.join(props)}})


  def delete(self, type, id):
    return self.send_request({'action': 'delete', 'method': 'DELETE',
                              'service': type, 'params': {'id': id}})


def process_pagination(response):
  response['items'] = response.get('items') or []
  response['pageToken'] = response.get('pageToken') or 0
  response['pageSize'] = response.get('pageSize') or 0
  response['totalSize'] = response.get('totalSize') or len(response['items'])

  response['start'] = (response['pageToken'] - 1) * response['pageSize']
  response['end'] = response['start'] + len(response['items'])
  if response['end'] > 0:
    response['start'] = response['start'] + 1

  return response


if __name__ == "__main__":
  with open('data/dd4_token-test.txt', 'r') as f:
    id_token = f.readline()
  dd4_service = DD4Service(id_token, True)
  dd4_service.get('patients', '6275798063382528')
  dd4_service.batch_get('patients', ['6275798063382528', '6262144785973248'])
  dd4_service.bulk_get('patients', ['6275798063382528', '6262144785973248'])
  dd4_service.list('patients', ['referralDate%3E1736160094000'])
  dd4_service.list_as_ids('patients', ['referralDate%3E1736160094000'])
  dd4_service.list('changeHistorys', ['entityType%3DPatient', 'timeStamp%3E1736160094000'])
  dd4_service.list_for_report('patients')
