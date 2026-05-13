package uz.khakimjanovich.homework.http.response;

public enum ApiResult {

    SUCCESS(0, "api.success"),
    VALIDATION_ERROR(1001, "api.validation_error"),
    NOT_FOUND(1004, "api.not_found"),
    BAD_REQUEST(1400, "api.bad_request"),
    INTERNAL_ERROR(1500, "api.internal_error");

    private final int status;
    private final String messageCode;

    ApiResult(int status, String messageCode) {
        this.status = status;
        this.messageCode = messageCode;
    }

    public int status() {
        return status;
    }

    public String messageCode() {
        return messageCode;
    }
}
