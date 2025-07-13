package com.eaglebank.eaglebank_api.model.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class BadRequestErrorResponseTest {

    @Test
    void testGettersAndSetters() {
        // Given
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        Details detail1 = new Details();
        detail1.setField("email");
        detail1.setMessage("must be a well-formed email address");
        detail1.setType("ConstraintViolation");
        details.add(detail1);

        BadRequestErrorResponse response = new BadRequestErrorResponse();

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(details, response.getDetails());
        assertEquals(1, response.getDetails().size());
        assertEquals("email", response.getDetails().get(0).getField());
        assertEquals("must be a well-formed email address", response.getDetails().get(0).getMessage());
        assertEquals("ConstraintViolation", response.getDetails().get(0).getType());
    }

    @Test
    void testValidBadRequestErrorResponse() {
        // Given
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        
        Details detail1 = new Details();
        detail1.setField("email");
        detail1.setMessage("must be a well-formed email address");
        detail1.setType("ConstraintViolation");
        details.add(detail1);
        
        Details detail2 = new Details();
        detail2.setField("phoneNumber");
        detail2.setMessage("must match the pattern '^\\+[1-9]\\d{1,14}$'");
        detail2.setType("ConstraintViolation");
        details.add(detail2);

        BadRequestErrorResponse response = new BadRequestErrorResponse();
        response.setMessage(message);
        response.setDetails(details);

        // When & Then
        assertEquals(message, response.getMessage());
        assertEquals(2, response.getDetails().size());
        assertEquals("email", response.getDetails().get(0).getField());
        assertEquals("phoneNumber", response.getDetails().get(1).getField());
    }

    @Test
    void testNullMessage() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        List<Details> details = new ArrayList<>();
        Details detail = new Details();
        detail.setField("email");
        detail.setMessage("must be a well-formed email address");
        detail.setType("ConstraintViolation");
        details.add(detail);

        // When
        response.setMessage(null);
        response.setDetails(details);

        // Then
        assertNull(response.getMessage());
        assertNotNull(response.getDetails());
        assertEquals(1, response.getDetails().size());
    }

    @Test
    void testNullDetails() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed";

        // When
        response.setMessage(message);
        response.setDetails(null);

        // Then
        assertEquals(message, response.getMessage());
        assertNull(response.getDetails());
    }

    @Test
    void testEmptyDetailsList() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertNotNull(response.getDetails());
        assertEquals(0, response.getDetails().size());
        assertTrue(response.getDetails().isEmpty());
    }

    @Test
    void testMultipleDetails() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Multiple validation errors occurred";
        List<Details> details = new ArrayList<>();
        
        // Create multiple detail objects
        for (int i = 1; i <= 5; i++) {
            Details detail = new Details();
            detail.setField("field" + i);
            detail.setMessage("Error message " + i);
            detail.setType("ConstraintViolation");
            details.add(detail);
        }

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(5, response.getDetails().size());
        
        for (int i = 0; i < 5; i++) {
            Details detail = response.getDetails().get(i);
            assertEquals("field" + (i + 1), detail.getField());
            assertEquals("Error message " + (i + 1), detail.getMessage());
            assertEquals("ConstraintViolation", detail.getType());
        }
    }

    @Test
    void testDetailsWithNullValues() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField(null);
        detail.setMessage(null);
        detail.setType(null);
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertNull(response.getDetails().get(0).getField());
        assertNull(response.getDetails().get(0).getMessage());
        assertNull(response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithEmptyStrings() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("");
        detail.setMessage("");
        detail.setType("");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("", response.getDetails().get(0).getField());
        assertEquals("", response.getDetails().get(0).getMessage());
        assertEquals("", response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithSpecialCharacters() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed with special characters: @#$%^&*()";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("user.email@domain.com");
        detail.setMessage("Field contains invalid characters: <script>alert('xss')</script>");
        detail.setType("ConstraintViolation<Email>");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("user.email@domain.com", response.getDetails().get(0).getField());
        assertEquals("Field contains invalid characters: <script>alert('xss')</script>", 
                    response.getDetails().get(0).getMessage());
        assertEquals("ConstraintViolation<Email>", response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithLongStrings() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "A very long error message that describes in detail what went wrong during the validation process and provides comprehensive information about the specific constraints that were violated";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("very.long.field.name.that.exceeds.normal.naming.conventions");
        detail.setMessage("This is an extremely detailed error message that provides comprehensive information about the validation failure, including specific details about what went wrong, why it went wrong, and how to fix it. The message is intentionally long to test the handling of large text content.");
        detail.setType("ConstraintViolation<Pattern<regexp=^\\+[1-9]\\d{1,14}$>>");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("very.long.field.name.that.exceeds.normal.naming.conventions", 
                    response.getDetails().get(0).getField());
        assertTrue(response.getDetails().get(0).getMessage().length() > 100);
        assertEquals("ConstraintViolation<Pattern<regexp=^\\+[1-9]\\d{1,14}$>>", 
                    response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithUnicodeCharacters() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation échoué avec des caractères spéciaux: ñáéíóú";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("user.email@dómäin.com");
        detail.setMessage("Le champ contient des caractères invalides: ñáéíóú");
        detail.setType("ConstraintViolation<Email>");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("user.email@dómäin.com", response.getDetails().get(0).getField());
        assertEquals("Le champ contient des caractères invalides: ñáéíóú", 
                    response.getDetails().get(0).getMessage());
        assertEquals("ConstraintViolation<Email>", response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithNumbers() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed with numeric field: 12345";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("field123");
        detail.setMessage("Error code: 400, Status: 12345");
        detail.setType("ConstraintViolation123");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("field123", response.getDetails().get(0).getField());
        assertEquals("Error code: 400, Status: 12345", response.getDetails().get(0).getMessage());
        assertEquals("ConstraintViolation123", response.getDetails().get(0).getType());
    }

    @Test
    void testDetailsWithMixedCase() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "VALIDATION FAILED WITH MIXED CASE";
        List<Details> details = new ArrayList<>();
        
        Details detail = new Details();
        detail.setField("User.Email@Domain.COM");
        detail.setMessage("ERROR MESSAGE WITH MIXED CASE");
        detail.setType("CONSTRAINTVIOLATION<EMAIL>");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);

        // Then
        assertEquals(message, response.getMessage());
        assertEquals(1, response.getDetails().size());
        assertEquals("User.Email@Domain.COM", response.getDetails().get(0).getField());
        assertEquals("ERROR MESSAGE WITH MIXED CASE", response.getDetails().get(0).getMessage());
        assertEquals("CONSTRAINTVIOLATION<EMAIL>", response.getDetails().get(0).getType());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        BadRequestErrorResponse response1 = new BadRequestErrorResponse();
        BadRequestErrorResponse response2 = new BadRequestErrorResponse();
        BadRequestErrorResponse response3 = new BadRequestErrorResponse();

        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        Details detail = new Details();
        detail.setField("email");
        detail.setMessage("must be a well-formed email address");
        detail.setType("ConstraintViolation");
        details.add(detail);

        // When
        response1.setMessage(message);
        response1.setDetails(details);
        
        response2.setMessage(message);
        response2.setDetails(details);
        
        response3.setMessage("Different message");
        response3.setDetails(details);

        // Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        BadRequestErrorResponse response = new BadRequestErrorResponse();
        String message = "Validation failed";
        List<Details> details = new ArrayList<>();
        Details detail = new Details();
        detail.setField("email");
        detail.setMessage("must be a well-formed email address");
        detail.setType("ConstraintViolation");
        details.add(detail);

        // When
        response.setMessage(message);
        response.setDetails(details);
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("BadRequestErrorResponse"));
        assertTrue(toString.contains("message="));
        assertTrue(toString.contains("details="));
    }
} 