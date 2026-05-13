package uz.khakimjanovich.homework.http.response;

import uz.khakimjanovich.homework.model.UserAccount;

public record UserResponse(
        Long id,
        String username,
        String displayName
) {

    public static UserResponse from(UserAccount user) {
        return new UserResponse(user.id(), user.username(), user.displayName());
    }
}

