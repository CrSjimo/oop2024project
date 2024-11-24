package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorCode> handle(ResponseException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode());
    }

}
