#!/bin/bash
# SIGTERM (15) triggers Spring Boot graceful shutdown: in-flight requests (bets, balance) can finish.
PID=$(cat ./pid.file 2>/dev/null)
if [ -z "$PID" ]; then
  echo "No pid.file found."
  exit 0
fi
kill -15 "$PID" 2>/dev/null || true