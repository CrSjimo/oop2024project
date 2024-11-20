package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorCode> handleEmailAlreadyExistsException(ResponseException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode());
    }
}
