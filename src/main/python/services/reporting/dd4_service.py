import json
from urllib.request import urlopen, Request

creds = {}
config = {'test': False}
PROD_API_BASE = 'https://ip360-179401.appspot.com/_api/{}/v1/{}?idToken={}'
TEST_API_BASE = 'https://test-dot-ip360-179401.uc.r.appspot.com/_api/{}/v1/{}?idToken={}'
#'https://test-dot-ip360-179401.uc.r.appspot.com/_api/patients/v1/get?id=6262144785973248&idToken=1266987330'


def send_request(req, id_token=None):
  if id_token is None and creds.get('id_token') is None:
    with open('dd4_token-test.txt' if config['test'] else 'dd4_token.txt', 'r') as f:
      creds['id_token'] = f.readline()

  api_base = TEST_API_BASE if config['test'] else PROD_API_BASE
  url = req.get('url') or api_base.format(req['service'], req['action'], id_token or creds['id_token'])
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


def create(type, entity, id_token=None):
  return send_request(
      {'action': 'create', 'method': 'POST', 'service': type, 'data': entity},
      id_token)


def get(type, id, id_token=None):
  return send_request(
      {'action': 'get', 'method': 'GET', 'service': type, 'params': {'id': id}},
      id_token)


def batch_get(type, ids, id_token=None):
  return send_request(
      {'action': 'batchGet', 'method': 'GET', 'service': type,
       'params': {'ids': ','.join(ids)}}, id_token)


def bulk_get(type, ids, id_token=None):
  return send_request({'action': 'bulkGet', 'method': 'POST', 'service': type,
                       'data': {'items': ids}}, id_token)


def list(type, filters=[], order_by=None, page_size=None, page_token=None, id_token=None):
  params = {
    "filter": ','.join(filters),
    'orderBy': order_by,
    'pageSize': page_size,
    'pageToken': page_token
  }

  return processPagination(send_request(
      {'action': 'list', 'method': 'GET', 'service': type, 'params': params}, id_token))


def list_as_ids(type, filters=[], order_by=None, page_size=None,
    page_token=None, id_token=None):
  params = {
    "filter": ','.join(filters),
    'orderBy': order_by,
    'pageSize': page_size,
    'pageToken': page_token
  }

  return processPagination(send_request(
      {'action': 'listAsIds', 'method': 'GET', 'service': type, 'params': params},
      id_token))


def list_for_report(type, filters=[], order_by=None, page_size=None,
    page_token=None, id_token=None):
  params = {
    "filter": ','.join(filters),
    'orderBy': order_by,
    'pageSize': page_size,
    'pageToken': page_token
  }

  return processPagination(send_request(
      {'action': 'listForReport', 'method': 'GET', 'service': type, 'params': params},
      id_token))


def search(type, params, id_token):
  return processPagination(send_request(
      {'action': 'search', 'method': 'GET', 'service': type, 'params': params},
      id_token))


def update(type, entity, props, id_token=None):
  updated = {}
  for p in props:
    updated[p] = entity[p]

  return send_request(
      {'action': 'update', 'method': 'PUT', 'service': type, 'data': updated,
       'params': {'id': entity['id'], 'updateMask': ','.join(props)}},
      id_token)


def delete(type, id, id_token):
  return send_request({'action': 'delete', 'method': 'DELETE', 'service': type,
                       'params': {'id': id}}, id_token)


def processPagination(response):
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
  with open('dd4_token-test.txt', 'r') as f:
    id_token = f.readline()
  get('patients', '6275798063382528', id_token)
  batch_get('patients', ['6275798063382528', '6262144785973248'], id_token)
  bulk_get('patients', ['6275798063382528', '6262144785973248'], id_token)
  list('patients', ['referralDate%3E1736160094000'], id_token=id_token)
  list_as_ids('patients', ['referralDate%3E1736160094000'], id_token=id_token)
  list('changeHistorys', ['entityType%3DPatient', 'timeStamp%3E1736160094000'], id_token=id_token)
  list_for_report('patients', id_token=id_token)
