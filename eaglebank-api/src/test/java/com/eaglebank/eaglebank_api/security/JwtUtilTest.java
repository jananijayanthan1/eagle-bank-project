package com.eaglebank.eaglebank_api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_ShouldContainUserIdAndEmail() {
        String userId = "usr-12345678";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        assertTrue(payload.contains(userId));
        assertTrue(payload.contains(email));
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmail() {
        String userId = "usr-12345678";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        String extractedEmail = jwtUtil.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void extractUserId_ShouldReturnCorrectUserId() {
        String userId = "usr-12345678";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        String extractedUserId = jwtUtil.extractUserId(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String userId = "usr-12345678";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        assertTrue(jwtUtil.validateToken(token, email));
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidEmail() {
        String userId = "usr-12345678";
        String email = "test@example.com";
        String token = jwtUtil.generateToken(userId, email);
        assertFalse(jwtUtil.validateToken(token, "wrong@example.com"));
    }

    @Test
    void validateToken_ShouldReturnFalseForMalformedToken() {
        String malformedToken = "abc.def";
        assertFalse(jwtUtil.validateToken(malformedToken, "test@example.com"));
    }

    @Test
    void isTokenExpired_ShouldReturnTrueForExpiredToken() {
        // Manually create a token with an expired exp claim
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"userId\":\"usr-12345678\",\"email\":\"test@example.com\",\"sub\":\"test@example.com\",\"iat\":\"0\",\"exp\":\"0\"}";
        String headerEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
        String payloadEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString((headerEncoded + "." + payloadEncoded + ".secret").getBytes());
        String expiredToken = headerEncoded + "." + payloadEncoded + "." + signature;
        assertTrue(jwtUtil.validateToken(expiredToken, "test@example.com") == false);
    }

    @Test
    void extractEmail_ShouldReturnNullForMalformedToken() {
        String malformedToken = "abc.def";
        assertNull(jwtUtil.extractEmail(malformedToken));
    }

    @Test
    void extractUserId_ShouldReturnNullForMalformedToken() {
        String malformedToken = "abc.def";
        assertNull(jwtUtil.extractUserId(malformedToken));
    }
} 