package dev.sjimo.oop2024project.utils;

import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException {
    private final ErrorCode errorCode;
    public ResponseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
