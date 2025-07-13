package com.eaglebank.eaglebank_api.model.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.eaglebank.eaglebank_api.enums.AccountType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

class CreateBankAccountRequestTest {

    private CreateBankAccountRequest request;
    private Validator validator;

    @BeforeEach
    void setUp() {
        request = new CreateBankAccountRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSetAndGetName() {
        request.setName("Personal Savings Account");
        assertEquals("Personal Savings Account", request.getName());
    }

    @Test
    void testSetAndGetAccountType() {
        request.setAccountType(AccountType.personal);
        assertEquals(AccountType.personal, request.getAccountType());
    }

    @Test
    void testCompleteRequest() {
        request.setName("Business Account");
        request.setAccountType(AccountType.personal);

        assertEquals("Business Account", request.getName());
        assertEquals(AccountType.personal, request.getAccountType());
    }

    @Test
    void testNullValues() {
        // Test that @NotNull validation fails when values are null
        request.setName(null);
        request.setAccountType(null);

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(request);
        
        assertEquals(2, violations.size());
        
        boolean hasNameViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        boolean hasAccountTypeViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("accountType"));
        
        assertTrue(hasNameViolation);
        assertTrue(hasAccountTypeViolation);
    }

    @Test
    void testEmptyString() {
        // Empty string is technically not null, so it passes @NotNull validation
        request.setName("");
        request.setAccountType(AccountType.personal);
        
        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(request);
        
        assertEquals(0, violations.size()); // Empty string passes @NotNull
        assertEquals("", request.getName());
    }

    @Test
    void testDifferentAccountNames() {
        // Test short name
        request.setName("Savings");
        assertEquals("Savings", request.getName());

        // Test long name
        String longName = "Premium Business Checking Account with Overdraft Protection";
        request.setName(longName);
        assertEquals(longName, request.getName());

        // Test name with special characters
        request.setName("Account-123_Test");
        assertEquals("Account-123_Test", request.getName());
    }

    @Test
    void testLombokGeneratedMethods() {
        // Test that Lombok @Data annotation generated all necessary methods
        CreateBankAccountRequest newRequest = new CreateBankAccountRequest();
        newRequest.setName("Test Account");
        newRequest.setAccountType(AccountType.personal);
        
        assertEquals("Test Account", newRequest.getName());
        assertEquals(AccountType.personal, newRequest.getAccountType());
    }

    @Test
    void testValidationScenarios() {
        // Test with valid data that satisfies @NotNull constraints
        request.setName("Valid Account Name");
        request.setAccountType(AccountType.personal);
        
        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(request);
        
        assertEquals(0, violations.size());
        assertNotNull(request.getName());
        assertNotNull(request.getAccountType());
        assertTrue(request.getName().length() > 0);
    }

    @Test
    void testEqualsAndHashCode() {
        CreateBankAccountRequest request1 = new CreateBankAccountRequest();
        request1.setName("Account 1");
        request1.setAccountType(AccountType.personal);

        CreateBankAccountRequest request2 = new CreateBankAccountRequest();
        request2.setName("Account 1");
        request2.setAccountType(AccountType.personal);

        CreateBankAccountRequest request3 = new CreateBankAccountRequest();
        request3.setName("Account 2");
        request3.setAccountType(AccountType.personal);

        // Test equals
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);

        // Test hashCode
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        request.setName("Test Account");
        request.setAccountType(AccountType.personal);
        
        String toString = request.toString();
        assertTrue(toString.contains("Test Account"));
        assertTrue(toString.contains("personal"));
        System.out.println("Tests were executed");
    }

    @Test
    void testPartialNullValidation() {
        // Test with only name null
        request.setName(null);
        request.setAccountType(AccountType.personal);
        
        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        
        // Test with only accountType null
        request.setName("Valid Name");
        request.setAccountType(null);
        
        violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("accountType")));
    }
} 