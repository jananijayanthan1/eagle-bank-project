package com.eaglebank.eaglebank_api.model.response;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class AuthResponseTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSetters() {
        AuthResponse resp = new AuthResponse();
        resp.setToken("dummy-jwt-token");
        assertEquals("dummy-jwt-token", resp.getToken());
    }

    @Test
    void equalsAndHashCode() {
        AuthResponse resp1 = new AuthResponse();
        resp1.setToken("dummy-jwt-token");
        AuthResponse resp2 = new AuthResponse();
        resp2.setToken("dummy-jwt-token");
        assertEquals(resp1, resp2);
        assertEquals(resp1.hashCode(), resp2.hashCode());
    }

    @Test
    void validationFailsForNullToken() {
        AuthResponse resp = new AuthResponse();
        Set<ConstraintViolation<AuthResponse>> violations = validator.validate(resp);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("token")));
    }

    @Test
    void validationPassesForValidToken() {
        AuthResponse resp = new AuthResponse();
        resp.setToken("dummy-jwt-token");
        Set<ConstraintViolation<AuthResponse>> violations = validator.validate(resp);
        assertTrue(violations.isEmpty());
    }
} 