package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Map<String, Object>> handle(ResponseException ex) {
        var body = new HashMap<String, Object>();
        body.put("code", ex.getErrorCode().getCode());
        body.put("message", ex.getErrorCode().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
