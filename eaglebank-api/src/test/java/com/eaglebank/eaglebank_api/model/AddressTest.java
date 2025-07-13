package com.eaglebank.eaglebank_api.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AddressTest {

    private Validator validator;
    private Address address;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        address = new Address();
    }

    @Test
    void testGettersAndSetters() {
        // Given
        String line1 = "123 Main St";
        String line2 = "Apt 4B";
        String line3 = "Building A";
        String town = "London";
        String county = "Greater London";
        String postcode = "SW1A 1AA";

        // When
        address.setLine1(line1);
        address.setLine2(line2);
        address.setLine3(line3);
        address.setTown(town);
        address.setCounty(county);
        address.setPostcode(postcode);

        // Then
        assertEquals(line1, address.getLine1());
        assertEquals(line2, address.getLine2());
        assertEquals(line3, address.getLine3());
        assertEquals(town, address.getTown());
        assertEquals(county, address.getCounty());
        assertEquals(postcode, address.getPostcode());
    }

    @Test
    void testValidAddress() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void testLine1NotNull() {
        // Given
        address.setLine1(null);
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("line1", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testLine2NotNull() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2(null);
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("line2", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testLine3NotNull() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3(null);
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("line3", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testTownNotNull() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown(null);
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("town", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testCountyNotNull() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty(null);
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("county", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testPostcodeNotNull() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode(null);

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Address> violation = violations.iterator().next();
        assertEquals("postcode", violation.getPropertyPath().toString());
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testMultipleViolations() {
        // Given
        address.setLine1(null);
        address.setLine2(null);
        address.setLine3(null);
        address.setTown(null);
        address.setCounty(null);
        address.setPostcode(null);

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertEquals(6, violations.size());
    }

    @Test
    void testAddressWithEmptyStrings() {
        // Given - Empty strings are not null, so they should pass validation
        address.setLine1("");
        address.setLine2("");
        address.setLine3("");
        address.setTown("");
        address.setCounty("");
        address.setPostcode("");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Empty strings should be valid (not null)");
    }

    @Test
    void testAddressWithSpecialCharacters() {
        // Given
        address.setLine1("123 Main St. #4");
        address.setLine2("Apt 4B-2");
        address.setLine3("Building A & B");
        address.setTown("St. John's");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Special characters should be valid");
    }

    @Test
    void testAddressWithUnicodeCharacters() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building A");
        address.setTown("Londres"); // French
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Unicode characters should be valid");
    }

    @Test
    void testAddressWithLongStrings() {
        // Given
        address.setLine1("123 Main Street, Apartment Complex, Building Number 4, Floor 5, Unit 6");
        address.setLine2("Additional Address Information, Suite 7, Office 8, Department 9");
        address.setLine3("More Address Details, Wing A, Section B, Area C, Zone D");
        address.setTown("Very Long Town Name That Exceeds Normal Length Expectations");
        address.setCounty("Extremely Long County Name That Goes Beyond Standard Naming Conventions");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Long strings should be valid");
    }

    @Test
    void testAddressWithNumbers() {
        // Given
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setLine3("Building 5");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Numbers in address should be valid");
    }

    @Test
    void testAddressWithMixedCase() {
        // Given
        address.setLine1("123 MAIN ST");
        address.setLine2("APT 4B");
        address.setLine3("BUILDING A");
        address.setTown("LONDON");
        address.setCounty("GREATER LONDON");
        address.setPostcode("sw1a 1aa");

        // When
        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        // Then
        assertTrue(violations.isEmpty(), "Mixed case should be valid");
    }
} 