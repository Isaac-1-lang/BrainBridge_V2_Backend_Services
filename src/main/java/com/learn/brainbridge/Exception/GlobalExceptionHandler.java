package com.learn.brainbridge.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionHandler - Handles exceptions globally across all controllers
 * 
 * CONCEPTS TO LEARN:
 * 1. @RestControllerAdvice - Intercepts exceptions from all @RestController classes
 *    - Similar to @ControllerAdvice but automatically converts to JSON
 * 2. @ExceptionHandler - Method that handles specific exception types
 * 3. Centralized Exception Handling - All exceptions handled in one place
 * 4. Consistent Error Response Format - All errors return same structure
 * 5. HTTP Status Codes - Map exceptions to appropriate HTTP status codes
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     * Returns HTTP 404 NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle BadRequestException
     * Returns HTTP 400 BAD_REQUEST
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation errors (@Valid annotation failures)
     * Returns HTTP 400 BAD_REQUEST with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        
        // Extract field errors from validation
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                // Class-level constraint (e.g. @PasswordMatches)
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");
        response.put("errors", errors);
        response.put("timestamp", LocalDateTime.now());
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle database unique/constraint violations (e.g., duplicate title/email)
     * Returns HTTP 409 CONFLICT
     */
    @ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraintViolation(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof DataIntegrityViolationException dive && dive.getMostSpecificCause() != null) {
            message = dive.getMostSpecificCause().getMessage();
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Constraint violation: " + message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handle all other exceptions (catch-all)
     * Returns HTTP 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * ErrorResponse - Standard error response structure
     */
    public static class ErrorResponse {
        private int status;
        private String message;
        private LocalDateTime timestamp;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        // Getters
        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}

