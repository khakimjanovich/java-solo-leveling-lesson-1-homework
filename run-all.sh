#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

./run-db.sh

cleanup() {
  if [[ -n "${API_PID:-}" ]]; then
    kill "$API_PID" >/dev/null 2>&1 || true
  fi
}
trap cleanup EXIT

echo "Starting Java Core Homework API on http://localhost:8090"
mvn spring-boot:run &
API_PID=$!

echo "Waiting for API to become ready..."
READY=0
for attempt in {1..40}; do
  if ! kill -0 "$API_PID" >/dev/null 2>&1; then
    echo "API process stopped before becoming ready."
    wait "$API_PID" || true
    exit 1
  fi
  if curl -fsS "http://localhost:8090/api/questions/random?topic=JAVA_CORE&difficulty=EASY" >/dev/null 2>&1; then
    echo "API is ready."
    READY=1
    break
  fi
  sleep 1
done

if [[ "$READY" != "1" ]]; then
  echo "API did not become ready in time."
  exit 1
fi

if [[ ! -t 0 ]]; then
  echo "API startup verified. No interactive terminal detected, so CLI was not launched."
  echo "Run ./run-all.sh from your terminal to open the CLI, or run ./run-cli.sh separately."
  exit 0
fi

mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli
