#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

./run-db.sh

echo "Starting Java Core Homework API on http://localhost:8090"
mvn spring-boot:run
