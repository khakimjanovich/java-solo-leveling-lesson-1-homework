package uz.khakimjanovich.homework.http.response;

import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.enums.QuestionType;
import uz.khakimjanovich.homework.model.Question;
import uz.khakimjanovich.homework.enums.Topic;

public record QuestionResponse(
        Long id,
        Topic topic,
        Difficulty difficulty,
        QuestionType type,
        String questionText,
        String optionA,
        String optionB,
        String optionC
) {

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.id(),
                question.topic(),
                question.difficulty(),
                question.type(),
                question.questionText(),
                question.optionA(),
                question.optionB(),
                question.optionC()
        );
    }
}
