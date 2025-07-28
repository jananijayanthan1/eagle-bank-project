package com.eaglebank.eaglebank_api.exception;

import com.eaglebank.eaglebank_api.model.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundException_ShouldReturn404Response() {
        // Given
        NotFoundException ex = new NotFoundException("Resource not found");
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // When
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleAuthenticationException_ShouldReturn401Response() {
        // Given
        AuthenticationException ex = new AuthenticationException("Invalid credentials");
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // When
        ResponseEntity<ErrorResponse> response = handler.handleAuthenticationException(ex, request);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().getStatus());
        assertEquals("Unauthorized", response.getBody().getError());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgumentException_ShouldReturn400Response() {
        // Given
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // When
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void handleRuntimeException_WithForbiddenMessage_ShouldReturn403Response() {
        // Given
        RuntimeException ex = new RuntimeException("Forbidden: user does not own this account");
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // When
        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex, request);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(403, response.getBody().getStatus());
        assertEquals("Forbidden", response.getBody().getError());
    }

    @Test
    void handleRuntimeException_WithoutForbiddenMessage_ShouldReturn500Response() {
        // Given
        RuntimeException ex = new RuntimeException("Some other error");
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // When
        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex, request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
    }
} 