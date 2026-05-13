package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

final class HomeworkApiClient {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpClient client;
    private final String api;

    HomeworkApiClient(String api) {
        this.client = HttpClient.newHttpClient();
        this.api = api;
    }

    JsonNode users() throws Exception {
        return get(api + "/api/users");
    }

    JsonNode userStats(Long userId) throws Exception {
        return get(api + "/api/users/" + userId + "/stats");
    }

    JsonNode session(String username, String displayName) throws Exception {
        String body = JSON.writeValueAsString(Map.of(
                "username", username,
                "displayName", displayName
        ));
        return sendJson(api + "/api/users/session", body);
    }

    JsonNode register(String username, String displayName) throws Exception {
        String body = JSON.writeValueAsString(Map.of(
                "username", username,
                "displayName", displayName
        ));
        return sendJson(api + "/api/users", body);
    }

    JsonNode randomQuestion(String topic, String difficulty) throws Exception {
        String url = api + "/api/questions/random?topic=" + encode(topic)
                + "&difficulty=" + encode(difficulty);
        return get(url);
    }

    JsonNode answer(Long userId, Long questionId, String answer) throws Exception {
        String body = JSON.writeValueAsString(Map.of(
                "userId", userId,
                "questionId", questionId,
                "answer", answer
        ));
        return sendJson(api + "/api/answers", body);
    }

    private JsonNode get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response);
    }

    private JsonNode sendJson(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response);
    }

    private JsonNode parseResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() >= 400) {
            throw new IllegalStateException("API failed with status " + response.statusCode() + ": " + response.body());
        }
        return JSON.readTree(response.body());
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

