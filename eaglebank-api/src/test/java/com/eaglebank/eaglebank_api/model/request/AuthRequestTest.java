package com.eaglebank.eaglebank_api.model.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class AuthRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSetters() {
        AuthRequest req = new AuthRequest();
        req.setEmail("jane.doe@example.com");
        req.setPassword("MySecurePassword123");
        assertEquals("jane.doe@example.com", req.getEmail());
        assertEquals("MySecurePassword123", req.getPassword());
    }

    @Test
    void equalsAndHashCode() {
        AuthRequest req1 = new AuthRequest();
        req1.setEmail("jane.doe@example.com");
        req1.setPassword("MySecurePassword123");
        AuthRequest req2 = new AuthRequest();
        req2.setEmail("jane.doe@example.com");
        req2.setPassword("MySecurePassword123");
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void validationFailsForNullFields() {
        AuthRequest req = new AuthRequest();
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validationFailsForInvalidEmail() {
        AuthRequest req = new AuthRequest();
        req.setEmail("not-an-email");
        req.setPassword("MySecurePassword123");
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(req);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validationPassesForValidFields() {
        AuthRequest req = new AuthRequest();
        req.setEmail("jane.doe@example.com");
        req.setPassword("MySecurePassword123");
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }
} 