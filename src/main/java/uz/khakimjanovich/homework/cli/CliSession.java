package uz.khakimjanovich.homework.cli;

import com.fasterxml.jackson.databind.JsonNode;

final class CliSession {

    private final Long userId;
    private final String username;
    private final String displayName;
    private JsonNode stats;

    CliSession(Long userId, String username, String displayName, JsonNode stats) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.stats = stats;
    }

    Long userId() {
        return userId;
    }

    String username() {
        return username;
    }

    String displayName() {
        return displayName;
    }

    JsonNode stats() {
        return stats;
    }

    void refreshStats(JsonNode stats) {
        this.stats = stats;
    }
}

