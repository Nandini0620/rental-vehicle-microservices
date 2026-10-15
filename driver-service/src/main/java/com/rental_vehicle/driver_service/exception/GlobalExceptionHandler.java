package com.rental_vehicle.driver_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
            return build(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors()
                    .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return build(HttpStatus.BAD_REQUEST, errors.toString());
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Map<String, Object>> handleDuplicate(DataIntegrityViolationException ex) {
            return build(HttpStatus.CONFLICT, "Duplicate or invalid data");
        }
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex)
        {
            return build(HttpStatus.BAD_REQUEST,
                    "Invalid request body. Vehicle type must be one of: BIKE, CAR, SUV, BUS, TRUCK");
        }

        private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put("message", message);
            return ResponseEntity.status(status).body(body);
        }
    }

