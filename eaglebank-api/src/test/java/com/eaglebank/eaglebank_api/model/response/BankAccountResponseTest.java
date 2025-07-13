package com.eaglebank.eaglebank_api.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

class BankAccountResponseTest {

    private BankAccountResponse response;
    private Validator validator;

    @BeforeEach
    void setUp() {
        response = new BankAccountResponse();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSetAndGetAccountNumber() {
        response.setAccountNumber("01234567");
        assertEquals("01234567", response.getAccountNumber());
    }

    @Test
    void testSetAndGetSortCode() {
        response.setSortCode("10-10-10");
        assertEquals("10-10-10", response.getSortCode());
    }

    @Test
    void testSetAndGetName() {
        response.setName("Personal Savings Account");
        assertEquals("Personal Savings Account", response.getName());
    }

    @Test
    void testSetAndGetAccountType() {
        response.setAccountType(AccountType.personal);
        assertEquals(AccountType.personal, response.getAccountType());
    }

    @Test
    void testSetAndGetBalance() {
        response.setBalance(1000.50);
        assertEquals(1000.50, response.getBalance());
    }

    @Test
    void testSetAndGetCurrency() {
        response.setCurrency(Currency.GBP);
        assertEquals(Currency.GBP, response.getCurrency());
    }

    @Test
    void testSetAndGetCreatedTimestamp() {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        response.setCreatedTimestamp(timestamp);
        assertEquals(timestamp, response.getCreatedTimestamp());
    }

    @Test
    void testSetAndGetUpdatedTimestamp() {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        response.setUpdatedTimestamp(timestamp);
        assertEquals(timestamp, response.getUpdatedTimestamp());
    }

    @Test
    void testCompleteResponse() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        response.setAccountNumber("01234567");
        response.setSortCode("10-10-10");
        response.setName("Business Account");
        response.setAccountType(AccountType.personal);
        response.setBalance(2500.75);
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(now);
        response.setUpdatedTimestamp(now);

        assertEquals("01234567", response.getAccountNumber());
        assertEquals("10-10-10", response.getSortCode());
        assertEquals("Business Account", response.getName());
        assertEquals(AccountType.personal, response.getAccountType());
        assertEquals(2500.75, response.getBalance());
        assertEquals(Currency.GBP, response.getCurrency());
        assertEquals(now, response.getCreatedTimestamp());
        assertEquals(now, response.getUpdatedTimestamp());
    }

    @Test
    void testNullValues() {
        // Test that @NotNull validation fails when values are null
        response.setAccountNumber(null);
        response.setSortCode(null);
        response.setName(null);
        response.setAccountType(null);
        response.setBalance(null);
        response.setCurrency(null);
        response.setCreatedTimestamp(null);
        response.setUpdatedTimestamp(null);

        Set<ConstraintViolation<BankAccountResponse>> violations = validator.validate(response);
        
        assertEquals(8, violations.size()); // All 8 fields have @NotNull
        
        // Verify each field has a violation
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("sortCode")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountType")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdTimestamp")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("updatedTimestamp")));
    }

    @Test
    void testAccountNumberPatternValidation() {
        // Test valid account numbers that match the pattern ^01\d{6}$
        response.setAccountNumber("01234567");
        assertEquals("01234567", response.getAccountNumber());

        response.setAccountNumber("01765432");
        assertEquals("01765432", response.getAccountNumber());

        response.setAccountNumber("01000000");
        assertEquals("01000000", response.getAccountNumber());
    }

    @Test
    void testInvalidAccountNumberPattern() {
        // Test invalid account numbers that don't match the pattern
        response.setAccountNumber("12345678"); // Doesn't start with 01
        response.setSortCode("10-10-10");
        response.setName("Test Account");
        response.setAccountType(AccountType.personal);
        response.setBalance(1000.00);
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        response.setUpdatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        Set<ConstraintViolation<BankAccountResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
    }

    @Test
    void testBalanceValidation() {
        // Test minimum balance (0.00)
        response.setBalance(0.00);
        assertEquals(0.00, response.getBalance());

        // Test maximum balance (10000.00)
        response.setBalance(10000.00);
        assertEquals(10000.00, response.getBalance());

        // Test decimal balance
        response.setBalance(1234.56);
        assertEquals(1234.56, response.getBalance());
    }

    @Test
    void testInvalidBalanceValidation() {
        // Test balance below minimum
        response.setAccountNumber("01234567");
        response.setSortCode("10-10-10");
        response.setName("Test Account");
        response.setAccountType(AccountType.personal);
        response.setBalance(-100.00); // Below minimum
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        response.setUpdatedTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

        Set<ConstraintViolation<BankAccountResponse>> violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("balance")));

        // Test balance above maximum
        response.setBalance(15000.00); // Above maximum
        
        violations = validator.validate(response);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("balance")));
    }

    @Test
    void testDifferentCurrencies() {
        response.setCurrency(Currency.GBP);
        assertEquals(Currency.GBP, response.getCurrency());

        response.setCurrency(Currency.USD);
        assertEquals(Currency.USD, response.getCurrency());

        response.setCurrency(Currency.EUR);
        assertEquals(Currency.EUR, response.getCurrency());
    }

    @Test
    void testDifferentAccountNames() {
        // Test short name
        response.setName("Savings");
        assertEquals("Savings", response.getName());

        // Test long name
        String longName = "Premium Business Checking Account with Overdraft Protection";
        response.setName(longName);
        assertEquals(longName, response.getName());

        // Test name with special characters
        response.setName("Account-123_Test");
        assertEquals("Account-123_Test", response.getName());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data annotation generated all necessary methods
        BankAccountResponse newResponse = new BankAccountResponse();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        newResponse.setAccountNumber("01234567");
        newResponse.setSortCode("10-10-10");
        newResponse.setName("Test Account");
        newResponse.setAccountType(AccountType.personal);
        newResponse.setBalance(1000.00);
        newResponse.setCurrency(Currency.GBP);
        newResponse.setCreatedTimestamp(now);
        newResponse.setUpdatedTimestamp(now);
        
        assertEquals("01234567", newResponse.getAccountNumber());
        assertEquals("10-10-10", newResponse.getSortCode());
        assertEquals("Test Account", newResponse.getName());
        assertEquals(AccountType.personal, newResponse.getAccountType());
        assertEquals(1000.00, newResponse.getBalance());
        assertEquals(Currency.GBP, newResponse.getCurrency());
        assertEquals(now, newResponse.getCreatedTimestamp());
        assertEquals(now, newResponse.getUpdatedTimestamp());
    }

    @Test
    void testValidationScenarios() {
        // Test with valid data that satisfies all @NotNull constraints
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        response.setAccountNumber("01234567");
        response.setSortCode("10-10-10");
        response.setName("Valid Account Name");
        response.setAccountType(AccountType.personal);
        response.setBalance(5000.00);
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(now);
        response.setUpdatedTimestamp(now);
        
        Set<ConstraintViolation<BankAccountResponse>> violations = validator.validate(response);
        
        assertEquals(0, violations.size()); // No violations for valid data
        
        assertNotNull(response.getAccountNumber());
        assertNotNull(response.getSortCode());
        assertNotNull(response.getName());
        assertNotNull(response.getAccountType());
        assertNotNull(response.getBalance());
        assertNotNull(response.getCurrency());
        assertNotNull(response.getCreatedTimestamp());
        assertNotNull(response.getUpdatedTimestamp());
        
        // Test pattern validation
        assertTrue(response.getAccountNumber().matches("^01\\d{6}$"));
        
        // Test balance constraints
        assertTrue(response.getBalance() >= 0.00);
        assertTrue(response.getBalance() <= 10000.00);
    }

    @Test
    void testEqualsAndHashCode() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        BankAccountResponse response1 = new BankAccountResponse();
        response1.setAccountNumber("01234567");
        response1.setSortCode("10-10-10");
        response1.setName("Account 1");
        response1.setAccountType(AccountType.personal);
        response1.setBalance(1000.00);
        response1.setCurrency(Currency.GBP);
        response1.setCreatedTimestamp(now);
        response1.setUpdatedTimestamp(now);

        BankAccountResponse response2 = new BankAccountResponse();
        response2.setAccountNumber("01234567");
        response2.setSortCode("10-10-10");
        response2.setName("Account 1");
        response2.setAccountType(AccountType.personal);
        response2.setBalance(1000.00);
        response2.setCurrency(Currency.GBP);
        response2.setCreatedTimestamp(now);
        response2.setUpdatedTimestamp(now);

        BankAccountResponse response3 = new BankAccountResponse();
        response3.setAccountNumber("01765432");
        response3.setSortCode("10-10-10");
        response3.setName("Account 2");
        response3.setAccountType(AccountType.personal);
        response3.setBalance(2000.00);
        response3.setCurrency(Currency.USD);
        response3.setCreatedTimestamp(now);
        response3.setUpdatedTimestamp(now);

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
        
        response.setAccountNumber("01234567");
        response.setSortCode("10-10-10");
        response.setName("Test Account");
        response.setAccountType(AccountType.personal);
        response.setBalance(1000.00);
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(now);
        response.setUpdatedTimestamp(now);
        
        String toString = response.toString();
        assertTrue(toString.contains("01234567"));
        assertTrue(toString.contains("10-10-10"));
        assertTrue(toString.contains("Test Account"));
        assertTrue(toString.contains("personal"));
        assertTrue(toString.contains("1000.0"));
        assertTrue(toString.contains("GBP"));
    }
} 