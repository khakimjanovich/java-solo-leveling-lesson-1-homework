package uz.khakimjanovich.homework.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.khakimjanovich.homework.http.response.ApiResponse;
import uz.khakimjanovich.homework.http.response.ApiResponseMapper;
import uz.khakimjanovich.homework.http.response.ApiResult;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final ApiResponseMapper responses;

    public ApiExceptionHandler(ApiResponseMapper responses) {
        this.responses = responses;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiResponse<Map<String, String>>> badRequest(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responses.response(ApiResult.BAD_REQUEST, Map.of("detail", exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Map<String, String>>> validation(MethodArgumentNotValidException exception) {
        String detail = exception.getFieldErrors().getFirst().getField()
                + " "
                + exception.getFieldErrors().getFirst().getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responses.response(ApiResult.VALIDATION_ERROR, Map.of("detail", detail)));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Map<String, String>>> internal(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responses.response(ApiResult.INTERNAL_ERROR, Map.of("detail", exception.getClass().getSimpleName())));
    }
}
