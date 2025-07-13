package com.eaglebank.eaglebank_api.model.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.eaglebank.eaglebank_api.enums.TransactionType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Currency;
import java.util.Set;

class CreateTransactionRequestTest {

    private CreateTransactionRequest request;
    private Validator validator;

    @BeforeEach
    void setUp() {
        request = new CreateTransactionRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSetAndGetAmount() {
        request.setAmount(100.50);
        assertEquals(100.50, request.getAmount());
    }

    @Test
    void testSetAndGetCurrency() {
        request.setCurrency(Currency.getInstance("GBP"));
        assertEquals(Currency.getInstance("GBP"), request.getCurrency());
    }

    @Test
    void testSetAndGetType() {
        request.setType(TransactionType.deposit);
        assertEquals(TransactionType.deposit, request.getType());
    }

    @Test
    void testCompleteRequest() {
        request.setAmount(250.75);
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.withdrawal);

        assertEquals(250.75, request.getAmount());
        assertEquals(Currency.getInstance("GBP"), request.getCurrency());
        assertEquals(TransactionType.withdrawal, request.getType());
    }

    @Test
    void testNullValues() {
        // Test that @NotNull validation fails when values are null
        request.setAmount(null);
        request.setCurrency(null);
        request.setType(null);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        
        assertEquals(3, violations.size());
        
        boolean hasAmountViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
        boolean hasCurrencyViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("currency"));
        boolean hasTypeViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("type"));
        
        assertTrue(hasAmountViolation);
        assertTrue(hasCurrencyViolation);
        assertTrue(hasTypeViolation);
    }

    @Test
    void testAmountValidation() {
        // Test minimum amount (0.00)
        request.setAmount(0.00);
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test maximum amount (10000.00)
        request.setAmount(10000.00);
        violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test decimal amount
        request.setAmount(1234.56);
        violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidAmountValidation() {
        // Test amount below minimum
        request.setAmount(-100.00); // Below minimum
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));

        // Test amount above maximum
        request.setAmount(15000.00); // Above maximum
        
        violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void testDifferentCurrencies() {
        request.setAmount(100.00);
        request.setType(TransactionType.deposit);

        // Test GBP
        request.setCurrency(Currency.getInstance("GBP"));
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test USD
        request.setCurrency(Currency.getInstance("USD"));
        violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test EUR
        request.setCurrency(Currency.getInstance("EUR"));
        violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    void testDifferentTransactionTypes() {
        request.setAmount(100.00);
        request.setCurrency(Currency.getInstance("GBP"));

        // Test deposit
        request.setType(TransactionType.deposit);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test withdrawal
        request.setType(TransactionType.withdrawal);
        violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    void testDifferentAmounts() {
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);

        // Test zero amount
        request.setAmount(0.0);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test large amount
        Double largeAmount = 9999.99;
        request.setAmount(largeAmount);
        violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test decimal amount
        Double decimalAmount = 123.45;
        request.setAmount(decimalAmount);
        violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data annotation generated all necessary methods
        CreateTransactionRequest newRequest = new CreateTransactionRequest();
        newRequest.setAmount(100.00);
        newRequest.setCurrency(Currency.getInstance("GBP"));
        newRequest.setType(TransactionType.deposit);
        
        assertEquals(100.00, newRequest.getAmount());
        assertEquals(Currency.getInstance("GBP"), newRequest.getCurrency());
        assertEquals(TransactionType.deposit, newRequest.getType());
    }

    @Test
    void testValidationScenarios() {
        // Test with valid data that satisfies all constraints
        request.setAmount(500.00);
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);
        
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        
        assertEquals(0, violations.size());
        assertNotNull(request.getAmount());
        assertNotNull(request.getCurrency());
        assertNotNull(request.getType());
        
        // Test amount constraints
        assertTrue(request.getAmount() >= 0.00);
        assertTrue(request.getAmount() <= 10000.00);
    }

    @Test
    void testPartialNullValidation() {
        // Test with only amount null
        request.setAmount(null);
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);
        
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
        
        // Test with only currency null
        request.setAmount(100.00);
        request.setCurrency(null);
        
        violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
        
        // Test with only type null
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(null);
        
        violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }

    @Test
    void testEqualsAndHashCode() {
        CreateTransactionRequest request1 = new CreateTransactionRequest();
        request1.setAmount(100.00);
        request1.setCurrency(Currency.getInstance("GBP"));
        request1.setType(TransactionType.deposit);

        CreateTransactionRequest request2 = new CreateTransactionRequest();
        request2.setAmount(100.00);
        request2.setCurrency(Currency.getInstance("GBP"));
        request2.setType(TransactionType.deposit);

        CreateTransactionRequest request3 = new CreateTransactionRequest();
        request3.setAmount(200.00);
        request3.setCurrency(Currency.getInstance("USD"));
        request3.setType(TransactionType.withdrawal);

        // Test equals
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);

        // Test hashCode
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        request.setAmount(100.00);
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);
        
        String toString = request.toString();
        assertTrue(toString.contains("100.0"));
        assertTrue(toString.contains("GBP"));
        assertTrue(toString.contains("deposit"));
    }

    @Test
    void testBoundaryAmountValues() {
        request.setCurrency(Currency.getInstance("GBP"));
        request.setType(TransactionType.deposit);

        // Test exact minimum (0.00)
        request.setAmount(0.00);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test exact maximum (10000.00)
        request.setAmount(10000.00);
        violations = validator.validate(request);
        assertEquals(0, violations.size());

        // Test just below minimum (-0.01)
        request.setAmount(-0.01);
        violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));

        // Test just above maximum (10000.01)
        request.setAmount(10000.01);
        violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }
} 