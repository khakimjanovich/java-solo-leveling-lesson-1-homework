package uz.khakimjanovich.homework.http.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.khakimjanovich.homework.http.request.SubmitAnswerRequest;
import uz.khakimjanovich.homework.http.response.AnswerResultResponse;
import uz.khakimjanovich.homework.service.HomeworkService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final HomeworkService service;

    public AnswerController(HomeworkService service) {
        this.service = service;
    }

    @PostMapping
    AnswerResultResponse answer(@Valid @RequestBody SubmitAnswerRequest request) {
        return service.answer(request);
    }
}

