package uz.khakimjanovich.homework.http.response;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
}
