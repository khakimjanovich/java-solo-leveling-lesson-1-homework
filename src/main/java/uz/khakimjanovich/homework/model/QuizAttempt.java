package uz.khakimjanovich.homework.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "given_answer", nullable = false)
    private String givenAnswer;

    @Column(name = "correct", nullable = false)
    private boolean correct;

    @Column(name = "answered_at", nullable = false)
    private Instant answeredAt = Instant.now();

    protected QuizAttempt() {
    }

    public QuizAttempt(UserAccount userAccount, Question question, String givenAnswer, boolean correct) {
        this.userAccount = userAccount;
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.correct = correct;
    }

    public Long id() {
        return id;
    }

    public boolean correct() {
        return correct;
    }

    public Instant answeredAt() {
        return answeredAt;
    }
}
