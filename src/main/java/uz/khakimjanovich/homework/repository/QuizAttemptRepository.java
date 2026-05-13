package uz.khakimjanovich.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.khakimjanovich.homework.model.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    long countByUserAccountId(Long userId);

    long countByUserAccountIdAndCorrectTrue(Long userId);
}
