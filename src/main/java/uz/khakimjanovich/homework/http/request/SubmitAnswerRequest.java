package uz.khakimjanovich.homework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmitAnswerRequest(
        @NotNull Long userId,
        @NotNull Long questionId,
        @NotBlank String answer
) {
}

