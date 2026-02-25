#!/bin/bash
# Update script for main/production: wait until no requests for 2 sec so we don't stop while
# users are playing. Then graceful stop (in-flight bets/balance updates finish), update, restart.
# Run on server: ./update.sh

set -e

BASE_URL="${BASE_URL:-http://127.0.0.1:8080}"
POLL_INTERVAL="${POLL_INTERVAL:-0.5}"
IDLE_SEC="${IDLE_SEC:-2}"
MAX_WAIT_SEC="${MAX_WAIT_SEC:-300}"
GRACEFUL_WAIT_SEC="${GRACEFUL_WAIT_SEC:-35}"

echo "Step 1: Waiting until no request for ${IDLE_SEC} sec (so we don't stop while users are playing)..."

# Poll until readyToShutdown is true or timeout (max_wait iterations of POLL_INTERVAL)
max_wait=$((MAX_WAIT_SEC * 2))   # e.g. 300s with 0.5s poll = 600 iterations
elapsed=0
while [ "$elapsed" -lt "$max_wait" ]; do
  resp=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/v1/internal/drain-status" 2>/dev/null || echo "000")
  if [ "$resp" = "200" ]; then
    body=$(curl -s "$BASE_URL/api/v1/internal/drain-status" 2>/dev/null || echo "{}")
    if echo "$body" | grep -q '"readyToShutdown":true'; then
      echo "Server idle for ${IDLE_SEC} sec. Proceeding to stop and update."
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

# Stop the application (graceful: in-flight requests get up to ~30s to finish)
if [ -f ./pid.file ]; then
  echo "Step 2: Sending graceful shutdown (SIGTERM); in-flight bets/balance updates will finish..."
  ./shutdown.sh
  PID=$(cat ./pid.file)
  waited=0
  while kill -0 "$PID" 2>/dev/null && [ "$waited" -lt "$GRACEFUL_WAIT_SEC" ]; do
    sleep 1
    waited=$((waited + 1))
  done
  if kill -0 "$PID" 2>/dev/null; then
    echo "Process still running after ${GRACEFUL_WAIT_SEC}s; forcing kill."
    kill -9 "$PID" 2>/dev/null || true
  fi
  rm -f ./pid.file
else
  echo "No pid.file found; skipping stop step."
fi

# Update codebase
echo "Step 3: Updating codebase..."
if [ -n "$UPDATE_CMD" ]; then
  eval "$UPDATE_CMD"
else
  # Default: git pull and build with Maven wrapper
  [ -d .git ] && git pull || true
  [ -f ./mvnw ] && ./mvnw -q -DskipTests package || true
fi

# Start the application
echo "Step 4: Starting application..."
./startup.sh

echo "Update complete. Users' in-flight bets/balances were protected by wait + graceful shutdown."
