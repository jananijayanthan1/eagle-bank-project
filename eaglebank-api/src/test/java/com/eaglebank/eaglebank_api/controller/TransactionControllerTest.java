package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;
import com.eaglebank.eaglebank_api.model.Transaction;
import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.repository.BankAccountRepository;
import com.eaglebank.eaglebank_api.model.request.CreateTransactionRequest;
import com.eaglebank.eaglebank_api.service.TransactionService;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String jwt = "test.jwt.token";
    private final String userId = "usr-abc123";
    private final String accountNumber = "01234567";
    private final String transactionId = "tan-xyz789";

    @BeforeEach
    void setup() {
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        // Mock the account lookup to return an account with the correct userId
        BankAccount account = new BankAccount();
        account.setAccountNumber(accountNumber);
        account.setUserId(userId);
        account.setBalance(1000.0);
        account.setCurrency(com.eaglebank.eaglebank_api.enums.Currency.GBP);
        account.setAccountType(com.eaglebank.eaglebank_api.enums.AccountType.personal);
        account.setName("Test Account");
        account.setSortCode("00-00-00");
        account.setCreatedTimestamp(OffsetDateTime.now());
        account.setUpdatedTimestamp(OffsetDateTime.now());
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
    }

    // @Test
    // void createTransaction_success() throws Exception {
    //     CreateTransactionRequest req = new CreateTransactionRequest();
    //     req.setAmount(100.0);
    //     req.setCurrency(java.util.Currency.getInstance("GBP"));
    //     req.setType(TransactionType.deposit);

    //     Transaction tx = new Transaction();
    //     tx.setId(transactionId);
    //     tx.setAccountNumber(accountNumber);
    //     tx.setUserId(userId);
    //     tx.setAmount(100.0);
    //     tx.setCurrency(com.eaglebank.eaglebank_api.enums.Currency.GBP);
    //     tx.setType(TransactionType.deposit);
    //     tx.setCreatedTimestamp(OffsetDateTime.now());

    //     when(transactionService.createTransaction(eq(accountNumber), eq(userId), any(CreateTransactionRequest.class)))
    //             .thenReturn(tx);

    //     mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(req)))
    //             .andExpect(status().isCreated())
    //             .andExpect(jsonPath("$.id").value(transactionId));
    // }

    // @Test
    // void listTransactions_success() throws Exception {
    //     Transaction tx = new Transaction();
    //     tx.setId(transactionId);
    //     tx.setAccountNumber(accountNumber);
    //     tx.setUserId(userId);
    //     tx.setAmount(100.0);
    //     tx.setCurrency(com.eaglebank.eaglebank_api.enums.Currency.GBP);
    //     tx.setType(TransactionType.deposit);
    //     tx.setCreatedTimestamp(OffsetDateTime.now());

    //     when(transactionService.listTransactions(accountNumber, userId))
    //             .thenReturn(Collections.singletonList(tx));

    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].id").value(transactionId));
    // }

    // @Test
    // void fetchTransactionById_success() throws Exception {
    //     Transaction tx = new Transaction();
    //     tx.setId(transactionId);
    //     tx.setAccountNumber(accountNumber);
    //     tx.setUserId(userId);
    //     tx.setAmount(100.0);
    //     tx.setCurrency(com.eaglebank.eaglebank_api.enums.Currency.GBP);
    //     tx.setType(TransactionType.deposit);
    //     tx.setCreatedTimestamp(OffsetDateTime.now());

    //     when(transactionService.fetchTransactionById(accountNumber, transactionId, userId))
    //             .thenReturn(tx);

    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, transactionId)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.id").value(transactionId));
    // }

    @Test
    void createTransaction_forbidden() throws Exception {
        when(jwtUtil.extractUserId(anyString())).thenReturn("other-user");
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.deposit);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    // @Test
    // void createTransaction_notFound() throws Exception {
    //     CreateTransactionRequest req = new CreateTransactionRequest();
    //     req.setAmount(100.0);
    //     req.setCurrency(java.util.Currency.getInstance("GBP"));
    //     req.setType(TransactionType.deposit);

    //     when(transactionService.createTransaction(eq(accountNumber), eq(userId), any(CreateTransactionRequest.class)))
    //             .thenThrow(new com.eaglebank.eaglebank_api.exception.NotFoundException("Bank account not found"));

    //     mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(req)))
    //             .andExpect(status().isNotFound());
    // }

    // @Test
    // void createTransaction_validationError() throws Exception {
    //     CreateTransactionRequest req = new CreateTransactionRequest();
    //     // Missing amount, currency, type
    //     mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(req)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // void listTransactions_forbidden() throws Exception {
    //     when(jwtUtil.extractUserId(anyString())).thenReturn("other-user");
    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isForbidden());
    // }

    // @Test
    // void listTransactions_notFound() throws Exception {
    //     when(transactionService.listTransactions(accountNumber, userId))
    //             .thenThrow(new com.eaglebank.eaglebank_api.exception.NotFoundException("Bank account not found"));
    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", accountNumber)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isNotFound());
    // }

    // @Test
    // void fetchTransactionById_forbidden() throws Exception {
    //     when(jwtUtil.extractUserId(anyString())).thenReturn("other-user");
    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, transactionId)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isForbidden());
    // }

    // @Test
    // void fetchTransactionById_notFound() throws Exception {
    //     when(transactionService.fetchTransactionById(accountNumber, transactionId, userId))
    //             .thenThrow(new com.eaglebank.eaglebank_api.exception.NotFoundException("Transaction not found"));
    //     mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, transactionId)
    //             .header("Authorization", "Bearer " + jwt))
    //             .andExpect(status().isNotFound());
    // }
} 