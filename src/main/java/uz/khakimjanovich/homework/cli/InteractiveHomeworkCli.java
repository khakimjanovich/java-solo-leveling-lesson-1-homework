package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Scanner;

import static uz.khakimjanovich.homework.cli.CliColors.RED;
import static uz.khakimjanovich.homework.cli.CliColors.RESET;
import static uz.khakimjanovich.homework.cli.CliColors.YELLOW;
import static uz.khakimjanovich.homework.cli.CliColors.DIM;

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
        CliSession session = selectOrCreateUser();
        JsonNode currentQuestion = null;
        String message = "";

        while (true) {
            view.clearScreen();
            view.printGameScreen(session, currentQuestion, message);
            message = "";
            char choice = view.readKey();
            try {
                switch (choice) {
                    case 'n' -> {
                        currentQuestion = randomQuestion(session);
                        message = "Question loaded. Press a to answer it.";
                    }
                    case 'a' -> {
                        message = answer(session, currentQuestion);
                        currentQuestion = null;
                    }
                    case 'u' -> {
                        session = selectOrCreateUser();
                        currentQuestion = null;
                        message = "User switched.";
                    }
                    case 'q' -> {
                        view.printMessageScreen("Goodbye", session, "Good luck with Java Core homework.");
                        return;
                    }
                    default -> message = "Unknown key. Use n, a, u, or q.";
                }
            } catch (Exception exception) {
                message = "Error: " + exception.getMessage();
            }
        }
    }

    private CliSession selectOrCreateUser() throws Exception {
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
            view.printMessageScreen("Select User", null, "No users yet. Create the first one.");
            view.prompt(scanner, DIM + "Press Enter to continue" + RESET, "");
        }
        return createUser();
    }

    private CliSession createUser() throws Exception {
        view.printCreateUserScreen();
        String username = view.prompt(scanner, "Username", "");
        String name = view.prompt(scanner, "Display name", username);
        JsonNode response = api.session(username, name);
        return new CliSession(
                response.get("id").asLong(),
                response.get("username").asText(),
                response.get("displayName").asText(),
                api.userStats(response.get("id").asLong())
        );
    }

    private JsonNode randomQuestion(CliSession session) throws Exception {
        String topic = view.chooseTopic(session);
        String difficulty = view.chooseDifficulty(session);
        return api.randomQuestion(topic, difficulty);
    }

    private String answer(CliSession session, JsonNode currentQuestion) throws Exception {
        view.printAnswerScreen(session, currentQuestion);
        if (currentQuestion == null) {
            view.prompt(scanner, DIM + "Press Enter to continue" + RESET, "");
            return "No active question. Press n first.";
        }
        String answer;
        if ("MULTIPLE_CHOICE".equals(currentQuestion.get("type").asText())) {
            answer = String.valueOf(view.readAnswerKey());
        } else {
            answer = view.prompt(scanner, "Your answer", "");
        }
        JsonNode response = api.answer(
                session.userId(),
                currentQuestion.get("id").asLong(),
                answer
        );
        session.refreshStats(api.userStats(session.userId()));
        view.printAnswerResultScreen(session, currentQuestion, response);
        view.prompt(scanner, DIM + "Press Enter to continue" + RESET, "");
        return response.get("correct").asBoolean() ? "Last answer was correct." : "Last answer was wrong.";
    }
}
