package uz.khakimjanovich.homework.http.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.khakimjanovich.homework.http.request.RegisterUserRequest;
import uz.khakimjanovich.homework.http.response.ApiResponse;
import uz.khakimjanovich.homework.http.response.ApiResponseMapper;
import uz.khakimjanovich.homework.http.response.UserResponse;
import uz.khakimjanovich.homework.http.response.UserStatsResponse;
import uz.khakimjanovich.homework.service.HomeworkService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final HomeworkService service;
    private final ApiResponseMapper responses;

    public UserController(HomeworkService service, ApiResponseMapper responses) {
        this.service = service;
        this.responses = responses;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> users() {
        return responses.success(service.users().stream()
                .map(UserResponse::from)
                .toList());
    }

    @GetMapping("/{id}/stats")
    ApiResponse<UserStatsResponse> stats(@PathVariable Long id) {
        return responses.success(service.stats(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        return responses.success(UserResponse.from(service.register(request)));
    }

    @PostMapping("/session")
    ApiResponse<UserResponse> session(@Valid @RequestBody RegisterUserRequest request) {
        return responses.success(UserResponse.from(service.session(request)));
    }
}
