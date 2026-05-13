package uz.khakimjanovich.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.model.Question;
import uz.khakimjanovich.homework.enums.Topic;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByTopicAndDifficulty(Topic topic, Difficulty difficulty);
}
