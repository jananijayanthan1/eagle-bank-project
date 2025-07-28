package com.eaglebank.eaglebank_api.exception;

import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Not Found");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(404);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(401);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Bad Request");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(400);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        
        // Check if it's a forbidden error (user doesn't own account)
        if (ex.getMessage() != null && ex.getMessage().contains("Forbidden")) {
            errorResponse.setError("Forbidden");
            errorResponse.setStatus(403);
        } else {
            errorResponse.setError("Internal Server Error");
            errorResponse.setStatus(500);
        }
        
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        HttpStatus status = errorResponse.getStatus() == 403 ? HttpStatus.FORBIDDEN : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Conflict");
        errorResponse.setMessage("The resource has been modified by another request. Please try again.");
        errorResponse.setStatus(409);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Validation Error");
        errorResponse.setMessage("Invalid input parameters");
        errorResponse.setStatus(400);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An unexpected error occurred");
        errorResponse.setStatus(500);
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 