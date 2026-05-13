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
import uz.khakimjanovich.homework.http.response.UserResponse;
import uz.khakimjanovich.homework.http.response.UserStatsResponse;
import uz.khakimjanovich.homework.service.HomeworkService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final HomeworkService service;

    public UserController(HomeworkService service) {
        this.service = service;
    }

    @GetMapping
    List<UserResponse> users() {
        return service.users().stream()
                .map(UserResponse::from)
                .toList();
    }

    @GetMapping("/{id}/stats")
    UserStatsResponse stats(@PathVariable Long id) {
        return service.stats(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        return UserResponse.from(service.register(request));
    }

    @PostMapping("/session")
    UserResponse session(@Valid @RequestBody RegisterUserRequest request) {
        return UserResponse.from(service.session(request));
    }
}
