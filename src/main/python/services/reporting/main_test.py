# Copyright 2018 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import main


def test_hello():
  main.app.testing = True
  client = main.app.test_client()

  r = client.get("/hello")
  assert r.status_code == 200
  assert "Hello from Python" in r.data.decode("utf-8")


def test_root():
  main.app.testing = True
  client = main.app.test_client()

  r = client.get("/")
  assert r.status_code == 501
  assert "Bad Request" in r.data.decode("utf-8")


def test_get_config():
  main.app.testing = True
  client = main.app.test_client()

  r = client.get("/get_config?reportId=1j6W4t7N__QdKwBAHKkHFdQC0SEdHgqnhkRtfsh9d9LQ")
  assert r.status_code == 200
  assert '"type":"Financial & Referral KPI","year":2024' in r.data.decode("utf-8")


def test_update():
  main.app.testing = True
  client = main.app.test_client()

  with open('data/dd4_token-test.txt', 'r') as f:
    id_token = f.readline()

  r = client.get(f'/update?reportId=1j6W4t7N__QdKwBAHKkHFdQC0SEdHgqnhkRtfsh9d9LQ&idToken={id_token}')
  assert r.status_code == 200
  assert '"id":"1j6W4t7N__QdKwBAHKkHFdQC0SEdHgqnhkRtfsh9d9LQ"' in r.data.decode("utf-8")

