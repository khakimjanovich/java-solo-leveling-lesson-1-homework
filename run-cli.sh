#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli

