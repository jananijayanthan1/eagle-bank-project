package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.exception.NotFoundException;
import com.eaglebank.eaglebank_api.model.request.CreateBankAccountRequest;
import com.eaglebank.eaglebank_api.model.response.BankAccountResponse;
import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import com.eaglebank.eaglebank_api.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBankAccountRequest validRequest;
    private BankAccountResponse validResponse;
    private String validToken;
    private String validUserId;
    private String validEmail;
    private final String validAccountNumber = "01012345";

    @BeforeEach
    void setUp() {
        validRequest = new CreateBankAccountRequest();
        validRequest.setName("Personal Bank Account");
        validRequest.setAccountType(AccountType.personal);

        validResponse = new BankAccountResponse();
        validResponse.setAccountNumber("01234567");
        validResponse.setSortCode("10-10-10");
        validResponse.setName("Personal Bank Account");
        validResponse.setAccountType(AccountType.personal);
        validResponse.setBalance(0.0);
        validResponse.setCurrency(Currency.GBP);
        validResponse.setCreatedTimestamp(OffsetDateTime.now());
        validResponse.setUpdatedTimestamp(OffsetDateTime.now());

        validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c3ItMTIzNDU2NzgiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0NTY3ODkwLCJleHAiOjE3MzQ2NTQyOTB9.signature";
        validUserId = "usr-12345678";
        validEmail = "test@example.com";
    }

    @Test
    void createAccount_ValidRequest_ReturnsCreated() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.createAccount(eq(validUserId), anyString(), any(CreateBankAccountRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(validResponse.getAccountNumber()))
                .andExpect(jsonPath("$.sortCode").value(validResponse.getSortCode()))
                .andExpect(jsonPath("$.name").value(validResponse.getName()))
                .andExpect(jsonPath("$.accountType").value(validResponse.getAccountType().toString()))
                .andExpect(jsonPath("$.balance").value(validResponse.getBalance()))
                .andExpect(jsonPath("$.currency").value(validResponse.getCurrency().toString()));
    }

    @Test
    void createAccount_InvalidToken_ReturnsUnauthorized() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or expired token"));
    }

    @Test
    void createAccount_MissingAuthorizationHeader_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_InvalidAuthorizationFormat_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "InvalidFormat " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is(401));
    }

    @Test
    void createAccount_TokenMissingUserId_ReturnsForbidden() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(null);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Token missing userId or email"));
    }

    @Test
    void createAccount_TokenMissingEmail_ReturnsForbidden() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(null);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Token missing userId or email"));
    }

    @Test
    void createAccount_MissingName_ReturnsBadRequest() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setAccountType(AccountType.personal);
        // name is null

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_EmptyName_ReturnsBadRequest() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setName("");
        invalidRequest.setAccountType(AccountType.personal);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().is(400));
    }

    @Test
    void createAccount_MissingAccountType_ReturnsBadRequest() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setName("Personal Bank Account");
        // accountType is null

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.createAccount(eq(validUserId), anyString(), any(CreateBankAccountRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unexpected error"));
    }

    @Test
    void createAccount_NullRequestBody_ReturnsBadRequest() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_EmptyRequestBody_ReturnsBadRequest() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_WrongContentType_ReturnsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.TEXT_PLAIN)
                .content("test"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void createAccount_ValidRequestWithSpecialCharacters_ReturnsCreated() throws Exception {
        CreateBankAccountRequest specialRequest = new CreateBankAccountRequest();
        specialRequest.setName("My Account & Savings");
        specialRequest.setAccountType(AccountType.personal);

        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.createAccount(eq(validUserId), anyString(), any(CreateBankAccountRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccount_ValidRequestWithLongName_ReturnsCreated() throws Exception {
        CreateBankAccountRequest longNameRequest = new CreateBankAccountRequest();
        longNameRequest.setName("Very Long Account Name That Exceeds Normal Length For Testing Purposes");
        longNameRequest.setAccountType(AccountType.personal);

        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.createAccount(eq(validUserId), anyString(), any(CreateBankAccountRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(longNameRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccount_ValidRequestWithWhitespaceName_ReturnsCreated() throws Exception {
        CreateBankAccountRequest whitespaceRequest = new CreateBankAccountRequest();
        whitespaceRequest.setName("   Account with whitespace   ");
        whitespaceRequest.setAccountType(AccountType.personal);

        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.createAccount(eq(validUserId), anyString(), any(CreateBankAccountRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(whitespaceRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccount_InvalidJson_ReturnsBadRequest() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_EmptyToken_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is(401));
    }

    @Test
    void createAccount_MalformedToken_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", "Bearer malformed.token.here")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().is(401));
    }

    @Test
    void listAccounts_ValidToken_ReturnsAccounts() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        var listResponse = new com.eaglebank.eaglebank_api.model.response.ListBankAccountsResponse();
        listResponse.setAccounts(java.util.List.of(validResponse));
        Mockito.when(accountService.listAccounts(validUserId)).thenReturn(listResponse);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts[0].accountNumber").value(validResponse.getAccountNumber()));
    }

    @Test
    void listAccounts_MissingToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Access token is missing or invalid"));
    }

    @Test
    void listAccounts_InvalidToken_ReturnsUnauthorized() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Access token is missing or invalid"));
    }

    @Test
    void fetchAccountByAccountNumber_ValidTokenAndOwnership_ReturnsAccount() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.fetchAccountByAccountNumber(validAccountNumber)).thenReturn(validResponse);
        var bankAccount = new com.eaglebank.eaglebank_api.model.BankAccount();
        bankAccount.setUserId(validUserId);
        Mockito.when(accountService.getBankAccountEntityByAccountNumber(validAccountNumber)).thenReturn(bankAccount);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/" + validAccountNumber)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(validResponse.getAccountNumber()));
    }

    @Test
    void fetchAccountByAccountNumber_AccountNotOwned_ReturnsForbidden() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.fetchAccountByAccountNumber(validAccountNumber)).thenReturn(validResponse);
        var bankAccount = new com.eaglebank.eaglebank_api.model.BankAccount();
        bankAccount.setUserId("other-user");
        Mockito.when(accountService.getBankAccountEntityByAccountNumber(validAccountNumber)).thenReturn(bankAccount);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/" + validAccountNumber)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("The user is not allowed to access the bank account details"));
    }

    @Test
    void fetchAccountByAccountNumber_NotFound_ReturnsNotFound() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        Mockito.when(accountService.fetchAccountByAccountNumber(validAccountNumber))
                .thenThrow(new NotFoundException("Bank account not found"));
        Mockito.when(accountService.getBankAccountEntityByAccountNumber(validAccountNumber))
                .thenThrow(new NotFoundException("Bank account not found"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/" + validAccountNumber)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void fetchAccountByAccountNumber_InvalidToken_ReturnsUnauthorized() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/" + validAccountNumber)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("The user was not authenticated"));
    }

    @Test
    void fetchAccountByAccountNumber_MissingToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/" + validAccountNumber))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("The user was not authenticated"));
    }

    @Test
    void fetchAccountByAccountNumber_InvalidAccountNumberFormat_ReturnsBadRequest() throws Exception {
        Mockito.when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        Mockito.when(jwtUtil.extractUserId(anyString())).thenReturn(validUserId);
        Mockito.when(jwtUtil.extractEmail(anyString())).thenReturn(validEmail);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/v1/accounts/invalid")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest());
    }
} 