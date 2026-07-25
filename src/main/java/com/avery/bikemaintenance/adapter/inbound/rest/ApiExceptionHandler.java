package com.avery.bikemaintenance.adapter.inbound.rest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.avery.bikemaintenance.application.service.InvalidCredentialsException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>>
            handleIllegalArgumentException(
                    IllegalArgumentException exception) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>>
            handleInvalidCredentialsException(
                    InvalidCredentialsException exception) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage());
    }

    private ResponseEntity<Map<String, Object>>
            buildResponse(
                    HttpStatus status,
                    String message) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now().toString());

        response.put(
                "status",
                status.value());

        response.put(
                "error",
                status.getReasonPhrase());

        response.put(
                "message",
                message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}