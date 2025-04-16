# Copyright 2018 Google LLC
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

# [START gae_python38_app]
# [START gae_python3_app]
from flask import Flask, request
from referrals_report import read_config, update_spreadsheet

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
    "Access-Control-Allow-Methods": "GET",
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


@app.route("/update")
def update():
  if request.method == "OPTIONS":
    return do_cors()

  return update_spreadsheet(request.args.get('report_id')), 200, HEADERS


@app.route("/get_config")
def get_config():
  return do_cors() if request.method == "OPTIONS" else read_config(request.args.get('report_id'))

