package uz.khakimjanovich.homework.http.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.khakimjanovich.homework.http.request.CreateQuestionRequest;
import uz.khakimjanovich.homework.http.response.ApiResponse;
import uz.khakimjanovich.homework.http.response.ApiResponseMapper;
import uz.khakimjanovich.homework.http.response.QuestionResponse;
import uz.khakimjanovich.homework.enums.Difficulty;
import uz.khakimjanovich.homework.enums.Topic;
import uz.khakimjanovich.homework.service.HomeworkService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final HomeworkService service;
    private final ApiResponseMapper responses;

    public QuestionController(HomeworkService service, ApiResponseMapper responses) {
        this.service = service;
        this.responses = responses;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<QuestionResponse> create(@Valid @RequestBody CreateQuestionRequest request) {
        return responses.success(QuestionResponse.from(service.createQuestion(request)));
    }

    @GetMapping("/random")
    ApiResponse<QuestionResponse> random(@RequestParam Topic topic, @RequestParam Difficulty difficulty) {
        return responses.success(QuestionResponse.from(service.randomQuestion(topic, difficulty)));
    }
}
