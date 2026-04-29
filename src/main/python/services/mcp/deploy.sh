uv lock
gcloud run deploy ip360-mcp-server \
  --project ip360-179401 \
  --region us-central1 \
  --source . \
  --env-vars-file env.yaml