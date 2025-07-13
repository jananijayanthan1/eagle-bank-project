package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.exception.AuthenticationException;
import com.eaglebank.eaglebank_api.model.request.AuthRequest;
import com.eaglebank.eaglebank_api.model.response.AuthResponse;
import com.eaglebank.eaglebank_api.service.AuthenticationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthRequest validRequest;
    private AuthResponse validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new AuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        validResponse = new AuthResponse();
        validResponse.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c3ItMTIzNDU2NzgiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0NTY3ODkwLCJleHAiOjE3MzQ2NTQyOTB9.signature");
    }

    @Test
    void authenticate_ValidRequest_ReturnsOkWithToken() throws Exception {
        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(validResponse.getToken()));
    }

    @Test
    void authenticate_UserNotFound_ReturnsUnauthorized() throws Exception {
        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenThrow(new AuthenticationException("User not found with email: test@example.com"));

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_InvalidPassword_ReturnsUnauthorized() throws Exception {
        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenThrow(new AuthenticationException("Invalid password"));

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_MissingEmail_ReturnsBadRequest() throws Exception {
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setPassword("password123");
        // email is null

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_MissingPassword_ReturnsBadRequest() throws Exception {
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setEmail("test@example.com");
        // password is null

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_InvalidEmailFormat_ReturnsBadRequest() throws Exception {
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setPassword("password123");

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_EmptyEmail_ReturnsOk() throws Exception {
        AuthRequest requestWithEmptyEmail = new AuthRequest();
        requestWithEmptyEmail.setEmail("");
        requestWithEmptyEmail.setPassword("password123");

        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithEmptyEmail)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticate_EmptyPassword_ReturnsOk() throws Exception {
        AuthRequest requestWithEmptyPassword = new AuthRequest();
        requestWithEmptyPassword.setEmail("test@example.com");
        requestWithEmptyPassword.setPassword("");

        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithEmptyPassword)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticate_NullRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_EmptyRequestBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_WrongContentType_ReturnsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.TEXT_PLAIN)
                .content("test"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void authenticate_ServiceThrowsRuntimeException_ReturnsUnauthorized() throws Exception {
        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_ValidRequestWithSpecialCharacters_ReturnsOk() throws Exception {
        AuthRequest specialRequest = new AuthRequest();
        specialRequest.setEmail("test+tag@example.com");
        specialRequest.setPassword("password!@#$%^&*()");

        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(validResponse.getToken()));
    }

    @Test
    void authenticate_ValidRequestWithLongEmail_ReturnsOk() throws Exception {
        AuthRequest longEmailRequest = new AuthRequest();
        longEmailRequest.setEmail("very.long.email.address.with.many.subdomains@example.com");
        longEmailRequest.setPassword("password123");

        Mockito.when(authenticationService.authenticate(any(AuthRequest.class)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(longEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(validResponse.getToken()));
    }
} 