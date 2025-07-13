package com.eaglebank.eaglebank_api.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

class TransactionResponseTest {

    private TransactionResponse response;
    private Validator validator;

    @BeforeEach
    void setUp() {
        response = new TransactionResponse();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSetAndGetId() {
        response.setId("tan-123abc");
        assertEquals("tan-123abc", response.getId());
    }

    @Test
    void testSetAndGetAmount() {
        response.setAmount(100.50);
        assertEquals(100.50, response.getAmount());
    }

    @Test
    void testSetAndGetCurrency() {
        response.setCurrency(Currency.GBP);
        assertEquals(Currency.GBP, response.getCurrency());
    }

    @Test
    void testSetAndGetType() {
        response.setType(TransactionType.deposit);
        assertEquals(TransactionType.deposit, response.getType());
    }

    @Test
    void testSetAndGetReference() {
        response.setReference("Salary payment");
        assertEquals("Salary payment", response.getReference());
    }

    @Test
    void testSetAndGetUserId() {
        response.setUserId("usr-abc123");
        assertEquals("usr-abc123", response.getUserId());
    }

    @Test
    void testSetAndGetCreatedTimestamp() {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        response.setCreatedTimestamp(timestamp);
        assertEquals(timestamp, response.getCreatedTimestamp());
    }

    @Test
    void testCompleteResponse() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        response.setId("tan-123abc");
        response.setAmount(250.75);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.withdrawal);
        response.setReference("ATM withdrawal");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(now);

        assertEquals("tan-123abc", response.getId());
        assertEquals(250.75, response.getAmount());
        assertEquals(Currency.GBP, response.getCurrency());
        assertEquals(TransactionType.withdrawal, response.getType());
        assertEquals("ATM withdrawal", response.getReference());
        assertEquals("usr-abc123", response.getUserId());
        assertEquals(now, response.getCreatedTimestamp());
    }

    @Test
    void testNullValues() {
        // Test that @NotNull validation fails when values are null
        response.setId(null);
        response.setAmount(null);
        response.setCurrency(null);
        response.setType(null);
        response.setReference(null);
        response.setUserId(null);
        response.setCreatedTimestamp(null);

        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(7, violations.size()); // All 7 fields have @NotNull
        
        // Verify each field has a violation
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("reference")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdTimestamp")));
    }

    @Test
    void testIdPatternValidation() {
        // Test valid IDs that match the pattern ^tan-[A-Za-z0-9]$
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        response.setId("tan-ABC123");
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        response.setId("tan-abc123");
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidIdPattern() {
        // Test invalid IDs that don't match the pattern
        response.setId("123abc"); // Doesn't start with tan-
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    @Test
    void testAmountValidation() {
        // Test valid amounts
        response.setId("tan-123abc");
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test minimum amount (0.00)
        response.setAmount(0.00);
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test maximum amount (10000.00)
        response.setAmount(10000.00);
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test decimal amount
        response.setAmount(1234.56);
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidAmountValidation() {
        // Test amount below minimum
        response.setId("tan-123abc");
        response.setAmount(-100.00); // Below minimum
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));

        // Test amount above maximum
        response.setAmount(15000.00); // Above maximum
        
        violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void testUserIdPatternValidation() {
        // Test valid user IDs that match the pattern ^usr-[A-Za-z0-9]+$
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        response.setUserId("usr-abc123");
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        response.setUserId("usr-ABC123");
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        response.setUserId("usr-123456");
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidUserIdPattern() {
        // Test invalid user IDs that don't match the pattern
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        response.setUserId("123abc"); // Doesn't start with usr-
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    void testDifferentCurrencies() {
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test GBP
        response.setCurrency(Currency.GBP);
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test USD
        response.setCurrency(Currency.USD);
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test EUR
        response.setCurrency(Currency.EUR);
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testDifferentTransactionTypes() {
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test deposit
        response.setType(TransactionType.deposit);
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test withdrawal
        response.setType(TransactionType.withdrawal);
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testDifferentAmounts() {
        response.setId("tan-123abc");
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test zero amount
        response.setAmount(0.0);
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test large amount
        Double largeAmount = 9999.99;
        response.setAmount(largeAmount);
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test decimal amount
        Double decimalAmount = 123.45;
        response.setAmount(decimalAmount);
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testDifferentReferences() {
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test short reference
        response.setReference("Salary");
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test long reference
        String longReference = "Premium Business Checking Account with Overdraft Protection";
        response.setReference(longReference);
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test reference with special characters
        response.setReference("Payment-123_Test");
        violations = validator.validate(response);
        assertEquals(0, violations.size());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data annotation generated all necessary methods
        TransactionResponse newResponse = new TransactionResponse();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        newResponse.setId("tan-123abc");
        newResponse.setAmount(100.00);
        newResponse.setCurrency(Currency.GBP);
        newResponse.setType(TransactionType.deposit);
        newResponse.setReference("Test payment");
        newResponse.setUserId("usr-abc123");
        newResponse.setCreatedTimestamp(now);
        
        assertEquals("tan-123abc", newResponse.getId());
        assertEquals(100.00, newResponse.getAmount());
        assertEquals(Currency.GBP, newResponse.getCurrency());
        assertEquals(TransactionType.deposit, newResponse.getType());
        assertEquals("Test payment", newResponse.getReference());
        assertEquals("usr-abc123", newResponse.getUserId());
        assertEquals(now, newResponse.getCreatedTimestamp());
    }

    @Test
    void testValidationScenarios() {
        // Test with valid data that satisfies all constraints
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        response.setId("tan-123abc");
        response.setAmount(500.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Valid transaction");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(now);
        
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(0, violations.size()); // No violations for valid data
        
        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getCurrency());
        assertNotNull(response.getType());
        assertNotNull(response.getReference());
        assertNotNull(response.getUserId());
        assertNotNull(response.getCreatedTimestamp());
        
        // Test pattern validation
        assertTrue(response.getId().matches("^tan-[A-Za-z0-9]+$"));
        assertTrue(response.getUserId().matches("^usr-[A-Za-z0-9]+$"));
        
        // Test amount constraints
        assertTrue(response.getAmount() >= 0.00);
        assertTrue(response.getAmount() <= 10000.00);
    }

    @Test
    void testPartialNullValidation() {
        // Test with only id null
        response.setId(null);
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        
        // Test with only amount null
        response.setId("tan-123abc");
        response.setAmount(null);
        
        violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
        
        // Test with only userId null
        response.setAmount(100.00);
        response.setUserId(null);
        
        violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    void testEqualsAndHashCode() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        TransactionResponse response1 = new TransactionResponse();
        response1.setId("tan-123abc");
        response1.setAmount(100.00);
        response1.setCurrency(Currency.GBP);
        response1.setType(TransactionType.deposit);
        response1.setReference("Test payment");
        response1.setUserId("usr-abc123");
        response1.setCreatedTimestamp(now);

        TransactionResponse response2 = new TransactionResponse();
        response2.setId("tan-123abc");
        response2.setAmount(100.00);
        response2.setCurrency(Currency.GBP);
        response2.setType(TransactionType.deposit);
        response2.setReference("Test payment");
        response2.setUserId("usr-abc123");
        response2.setCreatedTimestamp(now);

        TransactionResponse response3 = new TransactionResponse();
        response3.setId("tan-456def");
        response3.setAmount(200.00);
        response3.setCurrency(Currency.USD);
        response3.setType(TransactionType.withdrawal);
        response3.setReference("Different payment");
        response3.setUserId("usr-def456");
        response3.setCreatedTimestamp(now);

        // Test equals
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);

        // Test hashCode
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testToString() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        response.setId("tan-123abc");
        response.setAmount(100.00);
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test payment");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(now);
        
        String toString = response.toString();
        assertTrue(toString.contains("tan-123abc"));
        assertTrue(toString.contains("100.0"));
        assertTrue(toString.contains("GBP"));
        assertTrue(toString.contains("deposit"));
        assertTrue(toString.contains("Test payment"));
        assertTrue(toString.contains("usr-abc123"));
    }

    @Test
    void testBoundaryAmountValues() {
        response.setId("tan-123abc");
        response.setCurrency(Currency.GBP);
        response.setType(TransactionType.deposit);
        response.setReference("Test");
        response.setUserId("usr-abc123");
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        // Test exact minimum (0.00)
        response.setAmount(0.00);
        Set<ConstraintViolation<TransactionResponse>> violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test exact maximum (10000.00)
        response.setAmount(10000.00);
        violations = validator.validate(response);
        assertEquals(0, violations.size());

        // Test just below minimum (-0.01)
        response.setAmount(-0.01);
        violations = validator.validate(response);
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));

        // Test just above maximum (10000.01)
        response.setAmount(10000.01);
        violations = validator.validate(response);
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }
} 