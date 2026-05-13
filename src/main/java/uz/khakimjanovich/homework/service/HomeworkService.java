package uz.khakimjanovich.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.khakimjanovich.homework.http.request.CreateQuestionRequest;
import uz.khakimjanovich.homework.http.request.RegisterUserRequest;
import uz.khakimjanovich.homework.http.request.SubmitAnswerRequest;
import uz.khakimjanovich.homework.http.response.AnswerResultResponse;
import uz.khakimjanovich.homework.http.response.UserStatsResponse;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.model.Question;
import uz.khakimjanovich.homework.model.QuizAttempt;
import uz.khakimjanovich.homework.enums.Topic;
import uz.khakimjanovich.homework.model.UserAccount;
import uz.khakimjanovich.homework.repository.QuestionRepository;
import uz.khakimjanovich.homework.repository.QuizAttemptRepository;
import uz.khakimjanovich.homework.repository.UserAccountRepository;

import java.util.List;
import java.util.Random;

@Service
public class HomeworkService {

    private final UserAccountRepository users;
    private final QuestionRepository questions;
    private final QuizAttemptRepository attempts;
    private final Random random = new Random();

    public HomeworkService(UserAccountRepository users, QuestionRepository questions, QuizAttemptRepository attempts) {
        this.users = users;
        this.questions = questions;
        this.attempts = attempts;
    }

    @Transactional
    public UserAccount register(RegisterUserRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new IllegalArgumentException("username already exists");
        }
        return users.save(new UserAccount(request.username(), request.displayName()));
    }

    @Transactional
    public UserAccount session(RegisterUserRequest request) {
        return users.findByUsername(request.username())
                .orElseGet(() -> users.save(new UserAccount(request.username(), request.displayName())));
    }

    @Transactional(readOnly = true)
    public List<UserAccount> users() {
        return users.findAll();
    }

    @Transactional(readOnly = true)
    public UserStatsResponse stats(Long userId) {
        if (!users.existsById(userId)) {
            throw new IllegalArgumentException("user not found");
        }
        long total = attempts.countByUserAccountId(userId);
        if (total == 0) {
            return UserStatsResponse.empty(userId);
        }
        long correct = attempts.countByUserAccountIdAndCorrectTrue(userId);
        long wrong = total - correct;
        int accuracy = (int) Math.round((correct * 100.0) / total);
        return new UserStatsResponse(userId, total, correct, wrong, accuracy);
    }

    @Transactional
    public Question createQuestion(CreateQuestionRequest request) {
        return questions.save(new Question(
                request.topic(),
                request.difficulty(),
                request.type(),
                request.questionText(),
                request.optionA(),
                request.optionB(),
                request.optionC(),
                request.correctAnswer(),
                request.explanation()
        ));
    }

    @Transactional(readOnly = true)
    public Question randomQuestion(Topic topic, Difficulty difficulty) {
        List<Question> filtered = questions.findAllByTopicAndDifficulty(topic, difficulty);
        if (filtered.isEmpty()) {
            throw new IllegalArgumentException("no questions found for filters");
        }
        return filtered.get(random.nextInt(filtered.size()));
    }

    @Transactional
    public AnswerResultResponse answer(SubmitAnswerRequest request) {
        UserAccount user = users.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        Question question = questions.findById(request.questionId())
                .orElseThrow(() -> new IllegalArgumentException("question not found"));
        boolean correct = question.isCorrect(request.answer());
        QuizAttempt attempt = attempts.save(new QuizAttempt(user, question, request.answer(), correct));
        return new AnswerResultResponse(attempt.id(), correct, question.explanation());
    }
}
