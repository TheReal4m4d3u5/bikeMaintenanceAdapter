package com.avery.bikemaintenance.adapter.inbound.rest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.avery.bikemaintenance.application.exception.DuplicateEmailException;
import com.avery.bikemaintenance.application.exception.RepositoryException;
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

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>>
            handleIllegalStateException(
                    IllegalStateException exception) {

        return buildResponse(
                HttpStatus.CONFLICT,
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

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>>
            handleDuplicateEmailException(
                    DuplicateEmailException exception) {

        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage());
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<Map<String, Object>>
            handleRepositoryException(
                    RepositoryException exception) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The selected data provider could not complete the request.");
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

        response.put("message", message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
