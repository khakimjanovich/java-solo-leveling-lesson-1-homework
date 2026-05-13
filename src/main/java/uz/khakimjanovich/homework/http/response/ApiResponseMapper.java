package uz.khakimjanovich.homework.http.response;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseMapper {

    private final MessageSource messages;

    public ApiResponseMapper(MessageSource messages) {
        this.messages = messages;
    }

    public <T> ApiResponse<T> success(T data) {
        return response(ApiResult.SUCCESS, data);
    }

    public <T> ApiResponse<T> response(ApiResult result, T data) {
        return new ApiResponse<>(
                result.status(),
                messages.getMessage(result.messageCode(), null, LocaleContextHolder.getLocale()),
                data
        );
    }
}
