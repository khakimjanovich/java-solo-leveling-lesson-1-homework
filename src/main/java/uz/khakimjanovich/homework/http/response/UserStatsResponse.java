package uz.khakimjanovich.homework.http.response;

public record UserStatsResponse(
        Long userId,
        long totalAttempts,
        long correctAttempts,
        long wrongAttempts,
        int accuracyPercent
) {

    public static UserStatsResponse empty(Long userId) {
        return new UserStatsResponse(userId, 0, 0, 0, 0);
    }
}

