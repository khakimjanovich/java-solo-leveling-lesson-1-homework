package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import static uz.khakimjanovich.homework.cli.CliColors.BOLD;
import static uz.khakimjanovich.homework.cli.CliColors.CYAN;
import static uz.khakimjanovich.homework.cli.CliColors.DIM;
import static uz.khakimjanovich.homework.cli.CliColors.GREEN;
import static uz.khakimjanovich.homework.cli.CliColors.RED;
import static uz.khakimjanovich.homework.cli.CliColors.RESET;
import static uz.khakimjanovich.homework.cli.CliColors.YELLOW;

final class TerminalView {

    private static final int MIN_FRAME_INNER_WIDTH = 58;
    private static final int MAX_FRAME_INNER_WIDTH = 110;
    private static final int TERMINAL_MARGIN = 4;

    private static final String[][] TOPIC_OPTIONS = {
            {"JAVA_CORE", "Java Core"},
            {"OOP", "OOP"},
            {"COLLECTIONS", "Collections"},
            {"EXCEPTIONS", "Exceptions"},
            {"MAVEN", "Maven"}
    };

    private static final String[][] DIFFICULTY_OPTIONS = {
            {"EASY", "Easy"},
            {"MEDIUM", "Medium"},
            {"HARD", "Hard"}
    };

    String prompt(Scanner scanner, String label, String defaultValue) {
        String suffix = defaultValue.isBlank() ? "" : " [" + defaultValue + "]";
        System.out.print(CYAN + label + suffix + ": " + RESET);
        String value = scanner.nextLine().trim();
        return value.isBlank() ? defaultValue : value;
    }

    String chooseTopic(CliSession session) throws Exception {
        return selectOption(
                "Choose Topic",
                session,
                "Pick the Java Core area for the next random question.",
                "w/s move   Enter select   1-5 quick select",
                TOPIC_OPTIONS
        );
    }

    String chooseDifficulty(CliSession session) throws Exception {
        return selectOption(
                "Choose Difficulty",
                session,
                "Pick how hard this question should be.",
                "w/s move   Enter select   1-3 quick select",
                DIFFICULTY_OPTIONS
        );
    }

    void banner() {
        printBorder(BOLD + CYAN);
        printCenteredLine("Java Core Homework CLI Game", BOLD + CYAN);
        printBorder(BOLD + CYAN);
    }

    void printGameScreen(CliSession session, JsonNode currentQuestion, String message) {
        beginScreen("Current Session", session);
        if (!message.isBlank()) {
            printLeftLine(message, YELLOW);
            printEmptyLine("");
        }
        if (currentQuestion == null) {
            printCenteredLine("No active question yet.", DIM);
        } else {
            printQuestionBody(currentQuestion);
        }
        printEmptyLine(CYAN);
        printCenteredLine("Controls", BOLD);
        printCenteredLine("[n] next   [a] answer   [u] user   [q] quit", BOLD);
        printBorder(CYAN);
    }

    void section(String title) {
        System.out.println();
        printBorder(BOLD + YELLOW);
        printCenteredLine(title, BOLD + YELLOW);
        printBorder(BOLD + YELLOW);
    }

    int selectUser(JsonNode users, JsonNode[] statsByUser) throws Exception {
        int active = 0;
        int createNewIndex = users.size();

        while (true) {
            clearScreen();
            beginScreen("Select User", null);
            printCenteredLine("Use w/s or j/k to move, Enter to select, n to create.", DIM);
            printEmptyLine("");

            for (int i = 0; i < users.size(); i++) {
                printUserButton(i, users.get(i), statsByUser[i], i == active);
            }
            printCreateUserButton(active == createNewIndex);
            printCenteredLine("w/s move   Enter select   n new", BOLD);
            printBorder(CYAN);

            char key = readKey();
            if (key == 'w' || key == 'k') {
                active = active == 0 ? createNewIndex : active - 1;
            } else if (key == 's' || key == 'j') {
                active = active == createNewIndex ? 0 : active + 1;
            } else if (key == 'n') {
                return createNewIndex;
            } else if (key == '\n' || key == '\r') {
                return active;
            } else if (Character.isDigit(key)) {
                int selected = Character.getNumericValue(key) - 1;
                if (selected >= 0 && selected < users.size()) {
                    return selected;
                }
            }
        }
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    void printCreateUserScreen() {
        clearScreen();
        beginScreen("Create User", null);
        printCenteredLine("Create a learner profile for storing attempts.", DIM);
        printEmptyLine("");
        printLeftLine("Username: required", "");
        printLeftLine("Display name: optional, defaults to username", "");
        printEmptyLine("");
        printCenteredLine("Complete the prompts below this frame.", BOLD);
        printBorder(CYAN);
    }

    void printQuestion(JsonNode response) {
        printBorder(CYAN);
        printQuestionBody(response);
        printBorder(CYAN);
    }

    void printAnswerScreen(CliSession session, JsonNode currentQuestion) {
        clearScreen();
        beginScreen("Answer Question", session);
        if (currentQuestion == null) {
            printCenteredLine("No active question. Press n first.", YELLOW);
        } else {
            printQuestionBody(currentQuestion);
            printEmptyLine("");
            if ("MULTIPLE_CHOICE".equals(currentQuestion.get("type").asText())) {
                printCenteredLine("Press answer key: a, b, or c", BOLD);
            } else {
                printCenteredLine("Type your open answer below this frame.", BOLD);
            }
        }
        printBorder(CYAN);
    }

    void printAnswerResultScreen(CliSession session, JsonNode question, JsonNode result) {
        clearScreen();
        beginScreen("Answer Result", session);
        boolean correct = result.get("correct").asBoolean();
        printCenteredLine(correct ? "Correct" : "Wrong", correct ? GREEN : RED);
        printLeftLine("Attempt: #" + result.get("attemptId").asText(), "");
        if (question != null) {
            printLeftLine("Question: #" + question.get("id").asText(), "");
        }
        printEmptyLine("");
        printLeftLine("Explanation: " + result.get("explanation").asText(), YELLOW);
        printEmptyLine("");
        printCenteredLine("Press Enter to continue.", BOLD);
        printBorder(CYAN);
    }

    void printMessageScreen(String title, CliSession session, String message) {
        clearScreen();
        beginScreen(title, session);
        printCenteredLine(message, YELLOW);
        printEmptyLine("");
        printCenteredLine("Press Enter to continue.", BOLD);
        printBorder(CYAN);
    }

    String formatStats(JsonNode stats) {
        return "%d total, %d correct, %d wrong, %d%%".formatted(
                stats.get("totalAttempts").asLong(),
                stats.get("correctAttempts").asLong(),
                stats.get("wrongAttempts").asLong(),
                stats.get("accuracyPercent").asInt()
        );
    }

    char readKey() throws Exception {
        RawTerminal terminal = RawTerminal.enable();
        try {
            return Character.toLowerCase((char) System.in.read());
        } finally {
            terminal.close();
        }
    }

    char readAnswerKey() throws Exception {
        while (true) {
            char key = readKey();
            if (key == 'a' || key == 'b' || key == 'c') {
                return key;
            }
        }
    }

    private void printUserButton(int index, JsonNode user, JsonNode stats, boolean active) {
        String color = active ? GREEN : RESET;
        String marker = active ? ">" : " ";
        int contentWidth = innerWidth() - 4;
        System.out.print(color);
        printRawBorder();
        printRawLeftLine("%s %2d. %s".formatted(marker, index + 1, user.get("displayName").asText()), contentWidth);
        printRawLeftLine("  @" + user.get("username").asText(), contentWidth);
        printRawLeftLine("  Attempts: " + formatStats(stats), contentWidth);
        printRawBorder();
        System.out.print(RESET);
    }

    private void printCreateUserButton(boolean active) {
        String color = active ? GREEN : RESET;
        String marker = active ? ">" : " ";
        int contentWidth = innerWidth() - 4;
        System.out.print(color);
        printRawBorder();
        printRawLeftLine(marker + " + Create new user", contentWidth);
        printRawBorder();
        System.out.print(RESET);
    }

    private String selectOption(String title, CliSession session, String message, String footer, String[][] options) throws Exception {
        int active = 0;
        while (true) {
            clearScreen();
            beginScreen(title, session);
            printCenteredLine(message, DIM);
            printEmptyLine("");
            for (int i = 0; i < options.length; i++) {
                printOptionButton(i, options[i][1], i == active);
            }
            printEmptyLine("");
            printCenteredLine(footer, BOLD);
            printBorder(CYAN);

            char key = readKey();
            if (key == 'w' || key == 'k') {
                active = active == 0 ? options.length - 1 : active - 1;
            } else if (key == 's' || key == 'j') {
                active = active == options.length - 1 ? 0 : active + 1;
            } else if (key == '\n' || key == '\r') {
                return options[active][0];
            } else if (Character.isDigit(key)) {
                int selected = Character.getNumericValue(key) - 1;
                if (selected >= 0 && selected < options.length) {
                    return options[selected][0];
                }
            }
        }
    }

    private void printOptionButton(int index, String label, boolean active) {
        String color = active ? GREEN : RESET;
        String marker = active ? ">" : " ";
        printLeftLine("%s [%d] %s".formatted(marker, index + 1, label), color);
    }

    private void beginScreen(String title, CliSession session) {
        banner();
        printCenteredLine("Screen: " + title, BOLD + YELLOW);
        if (session != null) {
            printLeftLine("User: #%d %s (%s)".formatted(session.userId(), session.displayName(), session.username()), "");
            printLeftLine("Attempts: " + formatStats(session.stats()), "");
        }
        printBorder(CYAN);
    }

    private void printQuestionBody(JsonNode response) {
        printCenteredLine("Question #" + response.get("id").asText(), BOLD);
        printLeftLine("Topic: " + response.get("topic").asText(), "");
        printLeftLine("Difficulty: " + response.get("difficulty").asText(), "");
        printLeftLine("Type: " + response.get("type").asText(), "");
        printEmptyLine("");
        printLeftLine(response.get("questionText").asText(), BOLD);
        if ("MULTIPLE_CHOICE".equals(response.get("type").asText())) {
            printEmptyLine("");
            printLeftLine("a) " + response.get("optionA").asText(), YELLOW);
            printLeftLine("b) " + response.get("optionB").asText(), YELLOW);
            printLeftLine("c) " + response.get("optionC").asText(), YELLOW);
        }
    }

    private void printBorder(String color) {
        System.out.println(color + border() + RESET);
    }

    private void printRawBorder() {
        System.out.println(border());
    }

    private void printEmptyLine(String color) {
        System.out.println(color + "|" + " ".repeat(innerWidth()) + "|" + RESET);
    }

    private void printCenteredLine(String text, String color) {
        int width = innerWidth();
        String value = trim(text, width);
        int leftPadding = (width - value.length()) / 2;
        int rightPadding = width - value.length() - leftPadding;
        System.out.println(color + "|" + " ".repeat(leftPadding) + value + " ".repeat(rightPadding) + "|" + RESET);
    }

    private void printLeftLine(String text, String color) {
        int contentWidth = innerWidth() - 4;
        String value = trim(text, contentWidth);
        System.out.printf("%s|  %-" + contentWidth + "s  |%s%n", color, value, RESET);
    }

    private void printRawLeftLine(String text, int contentWidth) {
        String value = trim(text, contentWidth);
        System.out.printf("|  %-" + contentWidth + "s  |%n", value);
    }

    private String border() {
        return "+" + "-".repeat(innerWidth()) + "+";
    }

    private int innerWidth() {
        int terminalColumns = terminalColumns();
        int available = Math.max(MIN_FRAME_INNER_WIDTH, terminalColumns - TERMINAL_MARGIN - 2);
        return Math.min(MAX_FRAME_INNER_WIDTH, available);
    }

    private int terminalColumns() {
        String columns = System.getenv("COLUMNS");
        if (columns != null && !columns.isBlank()) {
            try {
                return Integer.parseInt(columns);
            } catch (NumberFormatException ignored) {
                // Fall through to tput.
            }
        }
        try {
            Process process = new ProcessBuilder("sh", "-c", "tput cols 2>/dev/null").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String value = reader.readLine();
                if (process.waitFor() == 0 && value != null && !value.isBlank()) {
                    return Integer.parseInt(value.trim());
                }
            }
        } catch (Exception ignored) {
            // Non-interactive shells may not expose terminal dimensions.
        }
        return MIN_FRAME_INNER_WIDTH + TERMINAL_MARGIN + 2;
    }

    private String trim(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        if (maxLength <= 3) {
            return value.substring(0, maxLength);
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
