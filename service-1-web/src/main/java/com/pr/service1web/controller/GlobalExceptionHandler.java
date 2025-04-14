package com.pr.service1web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handle(MissingRequestHeaderException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(exp.getMessage() + "  /api/client/login?userId=...");
    }
}
