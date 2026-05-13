package uz.khakimjanovich.homework.http.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
        @NotBlank String username,
        @NotBlank String displayName
) {
}

