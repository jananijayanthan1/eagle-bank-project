package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.exception.AuthenticationException;
import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.AuthRequest;
import com.eaglebank.eaglebank_api.model.response.AuthResponse;
import com.eaglebank.eaglebank_api.repository.UserRepository;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private AuthenticationService authenticationService;

    private AuthRequest validRequest;
    private User validUser;
    private AuthResponse expectedResponse;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtUtil);

        validRequest = new AuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        validUser = new User();
        validUser.setId("usr-12345678");
        validUser.setEmail("test@example.com");
        validUser.setPasswordHash("encodedPasswordHash");
        validUser.setName("Test User");
        validUser.setPhoneNumber("+447911123456");
        validUser.setAddress("{\"line1\":\"123 Main St\",\"postcode\":\"E1 6AN\"}");
        validUser.setCreatedTimestamp(OffsetDateTime.now());
        validUser.setUpdatedTimestamp(OffsetDateTime.now());

        expectedResponse = new AuthResponse();
        expectedResponse.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token");
    }

    @Test
    void authenticate_ValidCredentials_ReturnsAuthResponse() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(validUser);
        when(passwordEncoder.matches("password123", "encodedPasswordHash")).thenReturn(true);
        when(jwtUtil.generateToken("usr-12345678", "test@example.com")).thenReturn("test.jwt.token");

        // When
        AuthResponse result = authenticationService.authenticate(validRequest);

        // Then
        assertNotNull(result);
        assertEquals("test.jwt.token", result.getToken());
    }

    @Test
    void authenticate_UserNotFound_ThrowsAuthenticationException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(validRequest);
        });
        assertEquals("User not found with email: test@example.com", exception.getMessage());
    }

    @Test
    void authenticate_InvalidPassword_ThrowsAuthenticationException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(validUser);
        when(passwordEncoder.matches("password123", "encodedPasswordHash")).thenReturn(false);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(validRequest);
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void authenticate_DifferentEmail_ThrowsAuthenticationException() {
        // Given
        AuthRequest requestWithDifferentEmail = new AuthRequest();
        requestWithDifferentEmail.setEmail("different@example.com");
        requestWithDifferentEmail.setPassword("password123");

        when(userRepository.findByEmail("different@example.com")).thenReturn(null);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(requestWithDifferentEmail);
        });
        assertEquals("User not found with email: different@example.com", exception.getMessage());
    }

    @Test
    void authenticate_DifferentPassword_ThrowsAuthenticationException() {
        // Given
        AuthRequest requestWithDifferentPassword = new AuthRequest();
        requestWithDifferentPassword.setEmail("test@example.com");
        requestWithDifferentPassword.setPassword("wrongpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(validUser);
        when(passwordEncoder.matches("wrongpassword", "encodedPasswordHash")).thenReturn(false);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(requestWithDifferentPassword);
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void authenticate_EmptyEmail_ThrowsAuthenticationException() {
        // Given
        AuthRequest requestWithEmptyEmail = new AuthRequest();
        requestWithEmptyEmail.setEmail("");
        requestWithEmptyEmail.setPassword("password123");

        when(userRepository.findByEmail("")).thenReturn(null);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(requestWithEmptyEmail);
        });
        assertEquals("User not found with email: ", exception.getMessage());
    }

    @Test
    void authenticate_NullEmail_ThrowsAuthenticationException() {
        // Given
        AuthRequest requestWithNullEmail = new AuthRequest();
        requestWithNullEmail.setEmail(null);
        requestWithNullEmail.setPassword("password123");

        when(userRepository.findByEmail(null)).thenReturn(null);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(requestWithNullEmail);
        });
        assertEquals("User not found with email: null", exception.getMessage());
    }

    @Test
    void authenticate_ValidCredentialsWithSpecialCharacters_ReturnsAuthResponse() {
        // Given
        AuthRequest requestWithSpecialChars = new AuthRequest();
        requestWithSpecialChars.setEmail("test+tag@example.com");
        requestWithSpecialChars.setPassword("password!@#$%^&*()");

        User userWithSpecialEmail = new User();
        userWithSpecialEmail.setId("usr-87654321");
        userWithSpecialEmail.setEmail("test+tag@example.com");
        userWithSpecialEmail.setPasswordHash("encodedSpecialPasswordHash");

        when(userRepository.findByEmail("test+tag@example.com")).thenReturn(userWithSpecialEmail);
        when(passwordEncoder.matches("password!@#$%^&*()", "encodedSpecialPasswordHash")).thenReturn(true);
        when(jwtUtil.generateToken("usr-87654321", "test+tag@example.com")).thenReturn("special.jwt.token");

        // When
        AuthResponse result = authenticationService.authenticate(requestWithSpecialChars);

        // Then
        assertNotNull(result);
        assertEquals("special.jwt.token", result.getToken());
    }

    @Test
    void authenticate_ValidCredentialsWithLongEmail_ReturnsAuthResponse() {
        // Given
        String longEmail = "very.long.email.address.with.many.subdomains@example.com";
        AuthRequest requestWithLongEmail = new AuthRequest();
        requestWithLongEmail.setEmail(longEmail);
        requestWithLongEmail.setPassword("password123");

        User userWithLongEmail = new User();
        userWithLongEmail.setId("usr-long-email");
        userWithLongEmail.setEmail(longEmail);
        userWithLongEmail.setPasswordHash("encodedLongEmailPasswordHash");

        when(userRepository.findByEmail(longEmail)).thenReturn(userWithLongEmail);
        when(passwordEncoder.matches("password123", "encodedLongEmailPasswordHash")).thenReturn(true);
        when(jwtUtil.generateToken("usr-long-email", longEmail)).thenReturn("long.email.jwt.token");

        // When
        AuthResponse result = authenticationService.authenticate(requestWithLongEmail);

        // Then
        assertNotNull(result);
        assertEquals("long.email.jwt.token", result.getToken());
    }
} 