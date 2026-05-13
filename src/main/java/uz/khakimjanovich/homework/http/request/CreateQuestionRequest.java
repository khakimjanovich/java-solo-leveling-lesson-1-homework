package uz.khakimjanovich.homework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.enums.QuestionType;
import uz.khakimjanovich.homework.enums.Topic;

public record CreateQuestionRequest(
        @NotNull Topic topic,
        @NotNull Difficulty difficulty,
        @NotNull QuestionType type,
        @NotBlank String questionText,
        String optionA,
        String optionB,
        String optionC,
        @NotBlank String correctAnswer,
        @NotBlank String explanation
) {
}
