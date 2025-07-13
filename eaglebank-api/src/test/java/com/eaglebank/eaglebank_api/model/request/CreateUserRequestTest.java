package com.eaglebank.eaglebank_api.model.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.eaglebank.eaglebank_api.model.Address;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CreateUserRequestTest {

    private Validator validator;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword("TestPassword123"); // Set default valid password
    }

    @Test
    void testGettersAndSetters() {
        // Given
        String name = "John Doe";
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        String phoneNumber = "+44123456789";
        String email = "john.doe@example.com";
        String password = "TestPassword123";

        // When
        createUserRequest.setName(name);
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber(phoneNumber);
        createUserRequest.setEmail(email);
        createUserRequest.setPassword(password);

        // Then
        assertEquals(name, createUserRequest.getName());
        assertEquals(address, createUserRequest.getAddress());
        assertEquals(phoneNumber, createUserRequest.getPhoneNumber());
        assertEquals(email, createUserRequest.getEmail());
        assertEquals(password, createUserRequest.getPassword());
    }

    @Test
    void testValidCreateUserRequest() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testNameNotNull() {
        // Given
        createUserRequest.setName(null);
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testAddressNotNull() {
        // Given
        createUserRequest.setName("John Doe");
        createUserRequest.setAddress(null);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("address", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testPhoneNumberNotNull() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber(null);
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testPhoneNumberPatternValid() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertTrue(violations.isEmpty(), "Valid phone number should pass validation");
    }

    @Test
    void testPhoneNumberPatternInvalid() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("123456789"); // Missing + prefix
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
    }

    @Test
    void testEmailNotNull() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail(null);
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testEmailValid() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertTrue(violations.isEmpty(), "Valid email should pass validation");
    }

    @Test
    void testEmailInvalid() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("invalid-email");
        createUserRequest.setPassword("TestPassword123");

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void testPasswordNotNull() {
        // Given
        createUserRequest.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        createUserRequest.setAddress(address);
        createUserRequest.setPhoneNumber("+44123456789");
        createUserRequest.setEmail("john.doe@example.com");
        createUserRequest.setPassword(null);

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<CreateUserRequest> violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testMultipleViolations() {
        // Given
        createUserRequest.setName(null);
        createUserRequest.setAddress(null);
        createUserRequest.setPhoneNumber("invalid-phone");
        createUserRequest.setEmail("invalid-email");
        createUserRequest.setPassword(null);

        // When
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        // Then
        assertEquals(5, violations.size()); // name, address, phoneNumber, email, password
    }

    @Test
    void testPhoneNumberEdgeCases() {
        // Test various phone number formats
        String[] validPhoneNumbers = {
            "+1234567890",      // US format (10 digits)
            "+44123456789",     // UK format (11 digits)
            "+61412345678",     // Australian format (11 digits)
            "+81901234567"      // Japanese format (11 digits)
        };

        String[] invalidPhoneNumbers = {
            "1234567890",       // Missing +
            "+012345678",       // Starts with 0 (invalid first digit)
            "+1234567890123456789", // Too long (19 digits)
            "+123456789a",      // Contains letters
            "+123456789 ",      // Contains space
            ""                  // Empty string
        };

        // Test valid phone numbers
        for (String phoneNumber : validPhoneNumbers) {
            createUserRequest.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            createUserRequest.setAddress(address);
            createUserRequest.setPhoneNumber(phoneNumber);
            createUserRequest.setEmail("john.doe@example.com");
            createUserRequest.setPassword("TestPassword123");

            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            assertTrue(violations.isEmpty(), "Phone number " + phoneNumber + " should be valid");
        }

        // Test invalid phone numbers
        for (String phoneNumber : invalidPhoneNumbers) {
            createUserRequest.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            createUserRequest.setAddress(address);
            createUserRequest.setPhoneNumber(phoneNumber);
            createUserRequest.setEmail("john.doe@example.com");
            createUserRequest.setPassword("TestPassword123");

            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            assertFalse(violations.isEmpty(), "Phone number " + phoneNumber + " should be invalid");
        }
    }

    @Test
    void testEmailEdgeCases() {
        // Test various email formats
        String[] validEmails = {
            "john.doe@example.com",
            "jane.smith@company.co.uk",
            "user123@domain.org",
            "test+tag@example.com",
            "user.name@subdomain.example.com"
        };

        String[] invalidEmails = {
            "invalid-email",
            "@example.com",
            "user@",
            "user@.com",
            "user..name@example.com",
            "user@example..com"
        };

        // Test valid emails
        for (String email : validEmails) {
            createUserRequest.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            createUserRequest.setAddress(address);
            createUserRequest.setPhoneNumber("+44123456789");
            createUserRequest.setEmail(email);
            createUserRequest.setPassword("TestPassword123");

            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
        }

        // Test invalid emails
        for (String email : invalidEmails) {
            createUserRequest.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            createUserRequest.setAddress(address);
            createUserRequest.setPhoneNumber("+44123456789");
            createUserRequest.setEmail(email);
            createUserRequest.setPassword("TestPassword123");

            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            assertFalse(violations.isEmpty(), "Email " + email + " should be invalid");
        }
    }


} 