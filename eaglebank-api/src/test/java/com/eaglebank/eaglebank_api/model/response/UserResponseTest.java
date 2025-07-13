package com.eaglebank.eaglebank_api.model.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.eaglebank.eaglebank_api.model.Address;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserResponseTest {

    private Validator validator;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        
        userResponse = new UserResponse();
    }

    @Test
    void testGettersAndSetters() {
        // Given
        String id = "usr-123456789";
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
        OffsetDateTime createdTimestamp = OffsetDateTime.now();
        OffsetDateTime updatedTimestamp = OffsetDateTime.now();

        // When
        userResponse.setId(id);
        userResponse.setName(name);
        userResponse.setAddress(address);
        userResponse.setPhoneNumber(phoneNumber);
        userResponse.setEmail(email);
        userResponse.setCreatedTimestamp(createdTimestamp);
        userResponse.setUpdatedTimestamp(updatedTimestamp);

        // Then
        assertEquals(id, userResponse.getId());
        assertEquals(name, userResponse.getName());
        assertEquals(address, userResponse.getAddress());
        assertEquals(phoneNumber, userResponse.getPhoneNumber());
        assertEquals(email, userResponse.getEmail());
        assertEquals(createdTimestamp, userResponse.getCreatedTimestamp());
        assertEquals(updatedTimestamp, userResponse.getUpdatedTimestamp());
    }

    @Test
    void testValidUserResponse() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testIdNotNull() {
        // Given
        userResponse.setId(null);
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testIdPatternValid() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertTrue(violations.isEmpty(), "Valid ID should pass validation");
    }

    @Test
    void testIdPatternInvalid() {
        // Given
        userResponse.setId("user-123456789"); // Missing 'usr-' prefix
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    void testNameNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName(null);
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testAddressNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        userResponse.setAddress(null);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("address", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testPhoneNumberNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber(null);
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testPhoneNumberPatternValid() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertTrue(violations.isEmpty(), "Valid phone number should pass validation");
    }

    @Test
    void testPhoneNumberPatternInvalid() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("123456789"); // Missing + prefix
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
    }

    @Test
    void testEmailNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail(null);
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testEmailValid() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertTrue(violations.isEmpty(), "Valid email should pass validation");
    }

    @Test
    void testEmailInvalid() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("invalid-email");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void testCreatedTimestampNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(null);
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("createdTimestamp", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testUpdatedTimestampNotNull() {
        // Given
        userResponse.setId("usr-123456789");
        userResponse.setName("John Doe");
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");
        userResponse.setAddress(address);
        userResponse.setPhoneNumber("+44123456789");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(null);

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserResponse> violation = violations.iterator().next();
        assertEquals("updatedTimestamp", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testMultipleViolations() {
        // Given
        userResponse.setId(null);
        userResponse.setName(null);
        userResponse.setAddress(null);
        userResponse.setPhoneNumber("invalid-phone");
        userResponse.setEmail("invalid-email");
        userResponse.setCreatedTimestamp(null);
        userResponse.setUpdatedTimestamp(null);

        // When
        Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);

        // Then
        assertEquals(7, violations.size());
    }

    @Test
    void testIdPatternEdgeCases() {
        // Test various ID formats
        String[] validIds = {
            "usr-123456789",
            "usr-ABC123DEF",
            "usr-abc123def",
            "usr-123ABC456DEF",
            "usr-12345678901234567890"
        };

        String[] invalidIds = {
            "user-123456789",    // Wrong prefix
            "usr123456789",      // Missing dash
            "usr-",              // No suffix
            "usr-123-456",       // Contains extra dash
            "usr-123@456",       // Contains special character
            "usr-123 456",       // Contains space
            "",                  // Empty string
            null                 // Null value
        };

        // Test valid IDs
        for (String id : validIds) {
            userResponse.setId(id);
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber("+44123456789");
            userResponse.setEmail("john.doe@example.com");
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
            assertTrue(violations.isEmpty(), "ID " + id + " should be valid");
        }

        // Test invalid IDs
        for (String id : invalidIds) {
            userResponse.setId(id);
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber("+44123456789");
            userResponse.setEmail("john.doe@example.com");
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
            assertFalse(violations.isEmpty(), "ID " + id + " should be invalid");
        }
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
            userResponse.setId("usr-123456789");
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber(phoneNumber);
            userResponse.setEmail("john.doe@example.com");
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
            assertTrue(violations.isEmpty(), "Phone number " + phoneNumber + " should be valid");
        }

        // Test invalid phone numbers
        for (String phoneNumber : invalidPhoneNumbers) {
            userResponse.setId("usr-123456789");
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber(phoneNumber);
            userResponse.setEmail("john.doe@example.com");
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
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
            userResponse.setId("usr-123456789");
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber("+44123456789");
            userResponse.setEmail(email);
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
            assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
        }

        // Test invalid emails
        for (String email : invalidEmails) {
            userResponse.setId("usr-123456789");
            userResponse.setName("John Doe");
            Address address = new Address();
            address.setLine1("123 Main St");
            address.setLine2("Apt 4B");
            address.setLine3("Building A");
            address.setTown("London");
            address.setCounty("Greater London");
            address.setPostcode("SW1A 1AA");
            userResponse.setAddress(address);
            userResponse.setPhoneNumber("+44123456789");
            userResponse.setEmail(email);
            userResponse.setCreatedTimestamp(OffsetDateTime.now());
            userResponse.setUpdatedTimestamp(OffsetDateTime.now());

            Set<ConstraintViolation<UserResponse>> violations = validator.validate(userResponse);
            assertFalse(violations.isEmpty(), "Email " + email + " should be invalid");
        }
    }
} 