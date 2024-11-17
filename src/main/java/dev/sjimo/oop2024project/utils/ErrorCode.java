package dev.sjimo.oop2024project.utils;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, 100, "User not exist"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, 101, "User already exist"),
    USER_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, 102, "User not verified"),
    USER_ALREADY_VERIFIED(HttpStatus.CONFLICT, 103, "User already verified"),
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, 104, "Authentication failed"),
    INVALID_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, 105, "Invalid verification"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, 106, "Permission denied"),
    VERIFICATION_TOO_FREQUENT(HttpStatus.TOO_MANY_REQUESTS, 107, "Verification too frequent"),
    FRIEND_CANNOT_SELF(HttpStatus.CONFLICT, 108, "Friend cannot be yourself"),
    BLOCKED_BY_TARGET(HttpStatus.FORBIDDEN, 108, "Blocked by target"),
    BLOCKED_BY_USER(HttpStatus.FORBIDDEN, 109, "Blocked by user"),
    ALREADY_BE_FRIEND(HttpStatus.CONFLICT, 110, "Already have friend"),
    FRIEND_APPLICATION_PENDING(HttpStatus.CONFLICT, 111, "Friend application pending"),
    NOT_BE_FRIEND(HttpStatus.CONFLICT, 112, "Not be friend"),
    ALREADY_BLOCKED(HttpStatus.CONFLICT, 113, "Already blocked"),
    NOT_BLOCKED(HttpStatus.CONFLICT, 114, "Not blocked"),
    FRIEND_APPLICATION_NOT_EXIST(HttpStatus.CONFLICT, 115, "Friend application not exist"),
    FRIEND_APPLICATION_SOLVED(HttpStatus.CONFLICT, 116, "Friend application solved"),;

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
