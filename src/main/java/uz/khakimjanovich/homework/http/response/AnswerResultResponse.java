package uz.khakimjanovich.homework.http.response;

public record AnswerResultResponse(
        Long attemptId,
        boolean correct,
        String explanation
) {
}

