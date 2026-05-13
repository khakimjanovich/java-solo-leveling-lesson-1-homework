package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Scanner;

import static uz.khakimjanovich.homework.cli.CliColors.BOLD;
import static uz.khakimjanovich.homework.cli.CliColors.CYAN;
import static uz.khakimjanovich.homework.cli.CliColors.DIM;
import static uz.khakimjanovich.homework.cli.CliColors.GREEN;
import static uz.khakimjanovich.homework.cli.CliColors.RED;
import static uz.khakimjanovich.homework.cli.CliColors.RESET;
import static uz.khakimjanovich.homework.cli.CliColors.YELLOW;

final class TerminalView {

    String prompt(Scanner scanner, String label, String defaultValue) {
        String suffix = defaultValue.isBlank() ? "" : " [" + defaultValue + "]";
        System.out.print(CYAN + label + suffix + ": " + RESET);
        String value = scanner.nextLine().trim();
        return value.isBlank() ? defaultValue : value;
    }

    String chooseTopic(Scanner scanner) {
        System.out.println("""
                Topic
                [1] JAVA_CORE      [2] OOP
                [3] COLLECTIONS    [4] EXCEPTIONS
                [5] MAVEN
                """);
        return switch (prompt(scanner, "Topic key", "1")) {
            case "2" -> "OOP";
            case "3" -> "COLLECTIONS";
            case "4" -> "EXCEPTIONS";
            case "5" -> "MAVEN";
            default -> "JAVA_CORE";
        };
    }

    String chooseDifficulty(Scanner scanner) {
        System.out.println("""
                Difficulty
                [1] EASY    [2] MEDIUM    [3] HARD
                """);
        return switch (prompt(scanner, "Difficulty key", "1")) {
            case "2" -> "MEDIUM";
            case "3" -> "HARD";
            default -> "EASY";
        };
    }

    void banner() {
        System.out.println(BOLD + CYAN + """
                =========================================
                 Java Core Homework CLI Game
                =========================================
                """ + RESET);
    }

    void printGameScreen(CliSession session, JsonNode currentQuestion) {
        System.out.printf("%sCurrent user:%s #%d %s (%s)%n%n",
                CYAN,
                RESET,
                session.userId(),
                session.displayName(),
                session.username());
        System.out.println(CYAN + "Attempts:" + RESET + " " + formatStats(session.stats()));
        System.out.println();
        if (currentQuestion == null) {
            System.out.println(DIM + "No active question yet." + RESET);
        } else {
            printQuestion(currentQuestion);
        }
        System.out.println(BOLD + """

                Controls
                [n] next question   [a] answer current   [u] switch user   [q] quit
                """ + RESET);
    }

    void section(String title) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "== " + title + " ==" + RESET);
    }

    int selectUser(JsonNode users, JsonNode[] statsByUser) throws Exception {
        int active = 0;
        int createNewIndex = users.size();

        while (true) {
            clearScreen();
            banner();
            section("Select User");
            System.out.println(DIM + "Use w/s or j/k to move, Enter to select, n to create." + RESET);
            System.out.println();

            for (int i = 0; i < users.size(); i++) {
                printUserButton(i, users.get(i), statsByUser[i], i == active);
            }
            printCreateUserButton(active == createNewIndex);

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

    void printQuestion(JsonNode response) {
        System.out.printf("""
                %sQuestion #%s%s
                %sTopic:%s %s
                %sDifficulty:%s %s
                %sType:%s %s

                %s%s%s
                """,
                BOLD,
                response.get("id").asText(),
                RESET,
                CYAN,
                RESET,
                response.get("topic").asText(),
                CYAN,
                RESET,
                response.get("difficulty").asText(),
                CYAN,
                RESET,
                response.get("type").asText(),
                BOLD,
                response.get("questionText").asText(),
                RESET);
        if ("MULTIPLE_CHOICE".equals(response.get("type").asText())) {
            System.out.printf("""
                    %sa)%s %s
                    %sb)%s %s
                    %sc)%s %s
                    %n""",
                    YELLOW,
                    RESET,
                    response.get("optionA").asText(),
                    YELLOW,
                    RESET,
                    response.get("optionB").asText(),
                    YELLOW,
                    RESET,
                    response.get("optionC").asText());
        } else {
            System.out.println();
        }
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
                System.out.println(YELLOW + String.valueOf(key) + RESET);
                return key;
            }
            System.out.println(RED + "Use a, b, or c." + RESET);
        }
    }

    private void printUserButton(int index, JsonNode user, JsonNode stats, boolean active) {
        String color = active ? GREEN : RESET;
        String marker = active ? ">" : " ";
        System.out.print(color);
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| %s %d. %-50s |%n", marker, index + 1, trim(user.get("displayName").asText(), 50));
        System.out.printf("|   @%-53s |%n", trim(user.get("username").asText(), 53));
        System.out.printf("|   %-56s |%n", trim("Attempts: " + formatStats(stats), 56));
        System.out.println("+----------------------------------------------------------+");
        System.out.print(RESET);
        System.out.println();
    }

    private void printCreateUserButton(boolean active) {
        String color = active ? GREEN : RESET;
        String marker = active ? ">" : " ";
        System.out.print(color);
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| %s + %-52s |%n", marker, "Create new user");
        System.out.println("+----------------------------------------------------------+");
        System.out.print(RESET);
        System.out.println();
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
