package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class HomeworkCli {

    private static final String DEFAULT_API = "http://localhost:8090";

    public static void main(String[] args) throws Exception {
        String apiUrl = apiUrl(args);
        HomeworkApiClient api = new HomeworkApiClient(apiUrl);
        TerminalView view = new TerminalView();

        if (args.length == 0 || "interactive".equals(args[0])) {
            new InteractiveHomeworkCli(api, view, new Scanner(System.in)).run();
            return;
        }

        Map<String, String> options = parseOptions(args);
        switch (args[0]) {
            case "register" -> register(api, options);
            case "random" -> random(api, view, options);
            case "answer" -> answer(api, options);
            default -> printHelp();
        }
    }

    private static void register(HomeworkApiClient api, Map<String, String> options) throws Exception {
        require(options, "username");
        require(options, "name");
        JsonNode response = api.register(options.get("username"), options.get("name"));
        System.out.printf("Registered user #%s: %s%n", response.get("id").asText(), response.get("displayName").asText());
    }

    private static void random(HomeworkApiClient api, TerminalView view, Map<String, String> options) throws Exception {
        require(options, "topic");
        require(options, "difficulty");
        JsonNode response = api.randomQuestion(options.get("topic"), options.get("difficulty"));
        view.printQuestion(response);
    }

    private static void answer(HomeworkApiClient api, Map<String, String> options) throws Exception {
        require(options, "user-id");
        require(options, "question-id");
        require(options, "answer");
        JsonNode response = api.answer(
                Long.parseLong(options.get("user-id")),
                Long.parseLong(options.get("question-id")),
                options.get("answer")
        );
        System.out.printf("""
                Attempt #%s
                Correct: %s
                Explanation: %s
                %n""",
                response.get("attemptId").asText(),
                response.get("correct").asBoolean(),
                response.get("explanation").asText());
    }

    private static String apiUrl(String[] args) {
        Map<String, String> options = parseOptions(args);
        return options.getOrDefault("api", DEFAULT_API);
    }

    private static Map<String, String> parseOptions(String[] args) {
        Map<String, String> options = new LinkedHashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--") && i + 1 < args.length) {
                options.put(args[i].substring(2), args[++i]);
            }
        }
        return options;
    }

    private static void require(Map<String, String> options, String key) {
        if (!options.containsKey(key)) {
            throw new IllegalArgumentException("missing --" + key);
        }
    }

    private static void printHelp() {
        System.out.println("""
                Java Core Homework CLI

                Commands:
                  interactive
                  register --username almaz --name Almaz
                  random --topic JAVA_CORE --difficulty EASY
                  answer --user-id 1 --question-id 1 --answer a

                Optional:
                  --api http://localhost:8090
                """);
    }
}

