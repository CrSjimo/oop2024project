package dev.sjimo.oop2024project.utils;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, 100, "User not exist"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, 101, "User already exist"),
    USER_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, 102, "User not verified"),
    USER_ALREADY_VERIFIED(HttpStatus.CONFLICT, 103, "User already verified"),
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, 104, "Authentication failed"),
    INVALID_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, 105, "Invalid verification"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, 106, "Permission denied");

    private final HttpStatus status;
    private final int code;
    private final String message;

    private ErrorCode(HttpStatus status, int code, String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
