# Java Core Homework Lab

Homework project for Module 1 of Java Solo Leveling 30.

It combines a small Spring Boot API with a Java CLI so students practice Java core concepts through a real question-answer workflow.

## What It Teaches

- Java classes, records, enums, collections, exceptions, and packages.
- Maven project structure.
- CLI arguments and Java `HttpClient`.
- Multiple-choice and open-question answer flows.
- Spring REST API, validation, JPA entities, repositories, PostgreSQL runtime persistence, and H2 test persistence.
- Laravel bridge: Artisan command + controller + model + migration vs Java CLI main class + Spring controller + entity/repository.

## Run API

Fast script:

```bash
./run-api.sh
```

This starts PostgreSQL through Docker Compose, then starts the Spring API.

Manual command:

```bash
./run-db.sh
mvn spring-boot:run
```

API runs on:

```text
http://localhost:8090
```

PostgreSQL runs on:

```text
localhost:5435
database: java_core_homework
username: homework
password: secret
```

Runtime data is stored in PostgreSQL. Users and quiz attempts survive API restarts because the app uses schema update mode for the local homework database.

## Register User From CLI

Interactive mode:

```bash
./run-cli.sh
```

Manual command:

```bash
mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli
```

Interactive mode shows existing users at startup. Select a user by number, or create one if none exists. The API URL is hidden and defaults to `http://localhost:8090`. Answers are saved under the active session user.
Existing users show attempt stats in the selection list and current session header.

The CLI works like a small terminal game:

```text
[n] next question
[a] answer current question
[u] switch user
[q] quit
```

For multiple-choice questions, answer by pressing `a`, `b`, or `c` directly.
Topic and difficulty are selected by number keys, so learners do not need to type enum values perfectly.

Or explicitly:

```bash
mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli \
  -Dexec.args="interactive"
```

Direct command mode:

```bash
mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli \
  -Dexec.args="register --username almaz --name Almaz"
```

## Run API And CLI Together

```bash
./run-all.sh
```

This starts the API in the background, waits until it responds, then opens the interactive CLI. When the CLI exits, the script stops the API process.

## Get Random Question

```bash
mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli \
  -Dexec.args="random --user-id 1 --topic JAVA_CORE --difficulty EASY"
```

## Answer Question

```bash
mvn -q exec:java \
  -Dexec.mainClass=uz.khakimjanovich.homework.cli.HomeworkCli \
  -Dexec.args="answer --user-id 1 --question-id 1 --answer a"
```

## API Endpoints

```text
POST /api/users
GET  /api/users
GET  /api/users/{id}/stats
POST /api/users/session
POST /api/questions
GET  /api/questions/random?topic=JAVA_CORE&difficulty=EASY
POST /api/answers
```

## Question Types

- `MULTIPLE_CHOICE`: has options `a`, `b`, and `c`; the correct answer is one of those letters.
- `OPEN`: has no options; the answer is checked against the expected text.
