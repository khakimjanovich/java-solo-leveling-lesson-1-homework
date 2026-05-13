package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Scanner;

import static uz.khakimjanovich.homework.cli.CliColors.GREEN;
import static uz.khakimjanovich.homework.cli.CliColors.RED;
import static uz.khakimjanovich.homework.cli.CliColors.RESET;
import static uz.khakimjanovich.homework.cli.CliColors.YELLOW;
import static uz.khakimjanovich.homework.cli.CliColors.DIM;
import static uz.khakimjanovich.homework.cli.CliColors.CYAN;

final class InteractiveHomeworkCli {

    private final HomeworkApiClient api;
    private final TerminalView view;
    private final Scanner scanner;

    InteractiveHomeworkCli(HomeworkApiClient api, TerminalView view, Scanner scanner) {
        this.api = api;
        this.view = view;
        this.scanner = scanner;
    }

    void run() throws Exception {
        view.clearScreen();
        view.banner();
        CliSession session = selectOrCreateUser();
        JsonNode currentQuestion = null;

        while (true) {
            view.clearScreen();
            view.banner();
            view.printGameScreen(session, currentQuestion);
            char choice = view.readKey();
            try {
                switch (choice) {
                    case 'n' -> currentQuestion = randomQuestion();
                    case 'a' -> {
                        answer(session, currentQuestion);
                        currentQuestion = null;
                    }
                    case 'u' -> {
                        session = selectOrCreateUser();
                        currentQuestion = null;
                    }
                    case 'q' -> {
                        System.out.println(GREEN + "Good luck with Java Core homework." + RESET);
                        return;
                    }
                    default -> System.out.println(RED + "Unknown key. Use n, a, u, or q." + RESET);
                }
            } catch (Exception exception) {
                System.out.println(RED + "Error: " + exception.getMessage() + RESET);
            }
            System.out.println();
            view.prompt(scanner, DIM + "Press Enter to continue" + RESET, "");
        }
    }

    private CliSession selectOrCreateUser() throws Exception {
        view.section("Select User");
        JsonNode users = api.users();
        if (users.isArray() && !users.isEmpty()) {
            JsonNode[] statsByUser = new JsonNode[users.size()];
            for (int i = 0; i < users.size(); i++) {
                statsByUser[i] = api.userStats(users.get(i).get("id").asLong());
            }
            int selected = view.selectUser(users, statsByUser);
            if (selected < users.size()) {
                JsonNode user = users.get(selected);
                return new CliSession(
                        user.get("id").asLong(),
                        user.get("username").asText(),
                        user.get("displayName").asText(),
                        statsByUser[selected]
                );
            }
        } else {
            System.out.println(YELLOW + "No users yet. Create the first one." + RESET);
        }
        return createUser();
    }

    private CliSession createUser() throws Exception {
        view.section("Create User");
        String username = view.prompt(scanner, "Username", "");
        String name = view.prompt(scanner, "Display name", username);
        JsonNode response = api.session(username, name);
        System.out.printf(GREEN + "Session user #%s: %s%n" + RESET,
                response.get("id").asText(),
                response.get("displayName").asText());
        return new CliSession(
                response.get("id").asLong(),
                response.get("username").asText(),
                response.get("displayName").asText(),
                api.userStats(response.get("id").asLong())
        );
    }

    private JsonNode randomQuestion() throws Exception {
        view.section("Random Question");
        String topic = view.chooseTopic(scanner);
        String difficulty = view.chooseDifficulty(scanner);
        JsonNode response = api.randomQuestion(topic, difficulty);
        view.printQuestion(response);
        return response;
    }

    private void answer(CliSession session, JsonNode currentQuestion) throws Exception {
        view.section("Answer Question");
        if (currentQuestion == null) {
            System.out.println(YELLOW + "No active question. Press n first." + RESET);
            return;
        }
        view.printQuestion(currentQuestion);
        String answer;
        if ("MULTIPLE_CHOICE".equals(currentQuestion.get("type").asText())) {
            System.out.println(CYAN + "Press answer key: [a] [b] [c]" + RESET);
            answer = String.valueOf(view.readAnswerKey());
        } else {
            answer = view.prompt(scanner, "Your answer", "");
        }
        JsonNode response = api.answer(
                session.userId(),
                currentQuestion.get("id").asLong(),
                answer
        );
        boolean correct = response.get("correct").asBoolean();
        session.refreshStats(api.userStats(session.userId()));
        System.out.printf("%sAttempt #%s: %s%s%n",
                correct ? GREEN : RED,
                response.get("attemptId").asText(),
                correct ? "Correct" : "Wrong",
                RESET);
        System.out.println(YELLOW + "Explanation: " + RESET + response.get("explanation").asText());
    }
}
