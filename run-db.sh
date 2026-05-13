#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is required to start PostgreSQL automatically."
  echo "Install Docker or provide DB_URL, DB_USERNAME, and DB_PASSWORD manually."
  exit 1
fi

echo "Starting PostgreSQL on localhost:5435"
docker compose up -d postgres

echo "Waiting for PostgreSQL to accept connections..."
for attempt in {1..30}; do
  if docker compose exec -T postgres pg_isready -U homework -d java_core_homework >/dev/null 2>&1; then
    echo "PostgreSQL is ready."
    exit 0
  fi
  sleep 1
done

echo "PostgreSQL did not become ready in time."
exit 1
