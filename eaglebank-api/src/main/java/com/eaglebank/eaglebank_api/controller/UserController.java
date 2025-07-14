package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.request.AuthRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import com.eaglebank.eaglebank_api.model.response.AuthResponse;
import com.eaglebank.eaglebank_api.model.response.BadRequestErrorResponse;
import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import com.eaglebank.eaglebank_api.service.UserService;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import com.eaglebank.eaglebank_api.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.eaglebank.eaglebank_api.model.response.Details;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            UserResponse userResponse = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BadRequestErrorResponse error = new BadRequestErrorResponse();
        error.setMessage("Invalid details supplied");
        List<Details> detailsList = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
            Details d = new Details();
            d.setField(fieldError.getField());
            d.setMessage(fieldError.getDefaultMessage());
            d.setType(fieldError.getCode());
            return d;
        }).collect(Collectors.toList());
        error.setDetails(detailsList);
        return ResponseEntity.badRequest().body(error);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> fetchUserByID(
            @PathVariable String userId,
            @RequestHeader("Authorization") String bearerToken) {
        // Validate userId format
        if (!userId.matches("^usr-[A-Za-z0-9]+$")) {
            BadRequestErrorResponse error = new BadRequestErrorResponse();
            error.setMessage("Invalid userId format");
            error.setDetails(null);
            return ResponseEntity.badRequest().body(error);
        }
        // Extract userId from JWT token
        String token = bearerToken.replaceFirst("Bearer ", "");
        String tokenUserId = jwtUtil.extractUserId(token);
        if (tokenUserId == null || !tokenUserId.equals(userId)) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Forbidden: token does not match requested userId");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        try {
            UserResponse userResponse = userService.fetchUserById(userId);
            return ResponseEntity.ok(userResponse);
        } catch (NotFoundException e) {
            ErrorResponse error = new ErrorResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
}
