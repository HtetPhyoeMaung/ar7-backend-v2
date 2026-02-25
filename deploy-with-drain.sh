#!/bin/bash
# Jenkins-friendly deploy with drain: wait until no requests for 2 sec, then graceful stop, update, restart.
# Use this when deploying via Jenkins (Docker). Run on the app server (e.g. via SSH from Jenkins).
# Requires: curl, docker compose. App must be reachable at BASE_URL (default http://127.0.0.1:8080).

set -e

BASE_URL="${BASE_URL:-http://127.0.0.1:8080}"
POLL_INTERVAL="${POLL_INTERVAL:-0.5}"
IDLE_SEC="${IDLE_SEC:-2}"
MAX_WAIT_SEC="${MAX_WAIT_SEC:-300}"
APP_SERVICE="${APP_SERVICE:-backend-app}"

echo "Step 1: Waiting until no request for ${IDLE_SEC} sec (drain check at ${BASE_URL})..."
max_wait=$((MAX_WAIT_SEC * 2))
elapsed=0
while [ "$elapsed" -lt "$max_wait" ]; do
  resp=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/v1/internal/drain-status" 2>/dev/null || echo "000")
  if [ "$resp" = "200" ]; then
    body=$(curl -s "$BASE_URL/api/v1/internal/drain-status" 2>/dev/null || echo "{}")
    if echo "$body" | grep -q '"readyToShutdown":true'; then
      echo "Server idle for ${IDLE_SEC} sec. Proceeding to stop and deploy."
      break
    fi
  fi
  sleep "$POLL_INTERVAL"
  elapsed=$((elapsed + 1))
done

if [ "$elapsed" -ge "$max_wait" ]; then
  echo "Timeout: server did not become idle within ${MAX_WAIT_SEC}s. Aborting."
  exit 1
fi

echo "Step 2: Stopping app container (graceful SIGTERM; in-flight requests will finish)..."
docker compose stop "$APP_SERVICE" || true

echo "Step 3: Rebuilding and starting (code already pulled by Jenkins)..."
docker compose up -d --build

echo "Step 4: Streaming logs for 15 seconds..."
timeout 15s docker compose logs -f "$APP_SERVICE" 2>/dev/null || true

echo "Deploy complete. In-flight bets/balances were protected by wait + graceful shutdown."
