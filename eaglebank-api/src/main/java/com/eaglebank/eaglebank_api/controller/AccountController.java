package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.model.request.CreateBankAccountRequest;
import com.eaglebank.eaglebank_api.model.response.BankAccountResponse;
import com.eaglebank.eaglebank_api.service.AccountService;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private static final String SORT_CODE = "123456"; // Dummy global sort code

    public AccountController(AccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody @Valid CreateBankAccountRequest request) {

        try {
            String token = bearerToken.replaceFirst("Bearer ", "");
            if (!jwtUtil.isTokenValid(token)) {
                ErrorResponse error = new ErrorResponse();
                error.setError("Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            String userId = jwtUtil.extractUserId(token);
            String email = jwtUtil.extractEmail(token);
            if (userId == null || email == null) {
                ErrorResponse error = new ErrorResponse();
                error.setError("Token missing userId or email");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            BankAccountResponse response = accountService.createAccount(userId, SORT_CODE, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<?> listAccounts(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
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
            error.setError("Token missing userId");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        try {
            return ResponseEntity.ok(accountService.listAccounts(userId));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> fetchAccountByAccountNumber(
            @PathVariable String accountNumber,
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            ErrorResponse error = new ErrorResponse();
            error.setError("The user was not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String token = bearerToken.replaceFirst("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            ErrorResponse error = new ErrorResponse();
            error.setError("The user was not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractEmail(token);
        if (userId == null || email == null) {
            ErrorResponse error = new ErrorResponse();
            error.setError("The user is not allowed to access the bank account details");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        // Validate account number format
        if (!accountNumber.matches("^01\\d{6}$")) {
            com.eaglebank.eaglebank_api.model.response.BadRequestErrorResponse error = new com.eaglebank.eaglebank_api.model.response.BadRequestErrorResponse();
            error.setMessage("The request didn't supply all the necessary data");
            error.setDetails(null);
            return ResponseEntity.badRequest().body(error);
        }
        try {
            BankAccountResponse response = accountService.fetchAccountByAccountNumber(accountNumber);
            // Only allow access if the account belongs to the user in the token
            com.eaglebank.eaglebank_api.model.BankAccount account = accountService.getBankAccountEntityByAccountNumber(accountNumber);
            if (!userId.equals(account.getUserId())) {
                ErrorResponse error = new ErrorResponse();
                error.setError("The user is not allowed to access the bank account details");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                ErrorResponse error = new ErrorResponse();
                error.setError("Bank account was not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            ErrorResponse error = new ErrorResponse();
            error.setError("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
