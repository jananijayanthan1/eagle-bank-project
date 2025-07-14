package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.exception.NotFoundException;
import com.eaglebank.eaglebank_api.model.Transaction;
import com.eaglebank.eaglebank_api.model.request.CreateTransactionRequest;
import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import com.eaglebank.eaglebank_api.service.TransactionService;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(
            @PathVariable String accountNumber,
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody @Valid CreateTransactionRequest request) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String token = bearerToken.replaceFirst("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Forbidden: token missing userId");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        try {
            Transaction transaction = transactionService.createTransaction(accountNumber, userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (NotFoundException e) {
            ErrorResponse error = new ErrorResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Insufficient funds to process transaction");
            return ResponseEntity.status(422).body(error);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden")) {
                ErrorResponse error = new ErrorResponse();
                error.setError("The user is not allowed to access the transaction");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<?> listTransactions(
            @PathVariable String accountNumber,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String token = bearerToken.replaceFirst("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Forbidden: token missing userId");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        try {
            List<Transaction> transactions = transactionService.listTransactions(accountNumber, userId);
            return ResponseEntity.ok(transactions);
        } catch (NotFoundException e) {
            ErrorResponse error = new ErrorResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden")) {
                ErrorResponse error = new ErrorResponse();
                error.setError("The user is not allowed to access the transactions");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> fetchTransactionById(
            @PathVariable String accountNumber,
            @PathVariable String transactionId,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String token = bearerToken.replaceFirst("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Access token is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Forbidden: token missing userId");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        try {
            Transaction transaction = transactionService.fetchTransactionById(accountNumber, transactionId, userId);
            return ResponseEntity.ok(transaction);
        } catch (NotFoundException e) {
            ErrorResponse error = new ErrorResponse();
            error.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden")) {
                ErrorResponse error = new ErrorResponse();
                error.setError("The user is not allowed to access the transaction");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
} 