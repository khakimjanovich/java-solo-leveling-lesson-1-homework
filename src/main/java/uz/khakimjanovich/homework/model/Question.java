package uz.khakimjanovich.homework.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.enums.QuestionType;
import uz.khakimjanovich.homework.enums.Topic;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic", nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestionType type;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    @Column(name = "option_a", length = 500)
    private String optionA;

    @Column(name = "option_b", length = 500)
    private String optionB;

    @Column(name = "option_c", length = 500)
    private String optionC;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(name = "explanation", nullable = false, length = 1000)
    private String explanation;

    protected Question() {
    }

    public Question(
            Topic topic,
            Difficulty difficulty,
            QuestionType type,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String correctAnswer,
            String explanation
    ) {
        this.topic = topic;
        this.difficulty = difficulty;
        this.type = type;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public Long id() {
        return id;
    }

    public Topic topic() {
        return topic;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    public QuestionType type() {
        return type;
    }

    public String questionText() {
        return questionText;
    }

    public String optionA() {
        return optionA;
    }

    public String optionB() {
        return optionB;
    }

    public String optionC() {
        return optionC;
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.trim().equalsIgnoreCase(answer.trim());
    }

    public String explanation() {
        return explanation;
    }
}
