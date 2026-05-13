package uz.khakimjanovich.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uz.khakimjanovich.homework.http.request.CreateQuestionRequest;
import uz.khakimjanovich.homework.http.request.RegisterUserRequest;
import uz.khakimjanovich.homework.http.request.SubmitAnswerRequest;
import uz.khakimjanovich.homework.http.response.AnswerResultResponse;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.enums.QuestionType;
import uz.khakimjanovich.homework.model.Question;
import uz.khakimjanovich.homework.enums.Topic;
import uz.khakimjanovich.homework.model.UserAccount;
import uz.khakimjanovich.homework.service.HomeworkService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HomeworkServiceTest {

    @Autowired
    private HomeworkService service;

    @Test
    void registersUserGetsRandomQuestionAndStoresAnswerAttempt() {
        UserAccount user = service.register(new RegisterUserRequest("almaz", "Almaz"));
        service.createQuestion(new CreateQuestionRequest(
                Topic.SPRING_BASICS,
                Difficulty.HARD,
                QuestionType.MULTIPLE_CHOICE,
                "What Java keyword creates a subclass relationship?",
                "implements",
                "extends",
                "inherits",
                "b",
                "A class extends another class to inherit from it."
        ));
        Question question = service.randomQuestion(Topic.SPRING_BASICS, Difficulty.HARD);

        AnswerResultResponse result = service.answer(new SubmitAnswerRequest(user.id(), question.id(), "b"));

        assertThat(result.attemptId()).isNotNull();
        assertThat(result.correct()).isTrue();
    }

    @Test
    void reusesUserForCliSessionByUsername() {
        UserAccount first = service.session(new RegisterUserRequest("session-user", "Session User"));
        UserAccount second = service.session(new RegisterUserRequest("session-user", "Ignored Name"));

        assertThat(second.id()).isEqualTo(first.id());
        assertThat(second.displayName()).isEqualTo("Session User");
    }

    @Test
    void checksOpenQuestionAnswer() {
        UserAccount user = service.register(new RegisterUserRequest("open-user", "Open User"));
        Question question = service.createQuestion(new CreateQuestionRequest(
                Topic.JAVA_CORE,
                Difficulty.EASY,
                QuestionType.OPEN,
                "What file defines Maven dependencies?",
                null,
                null,
                null,
                "pom.xml",
                "Maven projects are configured through pom.xml."
        ));

        AnswerResultResponse result = service.answer(new SubmitAnswerRequest(user.id(), question.id(), "pom.xml"));

        assertThat(result.attemptId()).isNotNull();
        assertThat(result.correct()).isTrue();
    }
}
