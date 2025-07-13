package com.eaglebank.eaglebank_api.model.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void testGettersAndSetters() {
        // Given
        String error = "Internal server error";
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testValidErrorResponse() {
        // Given
        String error = "Internal server error";
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testNullError() {
        // Given
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError(null);

        // Then
        assertNull(response.getError());
    }

    @Test
    void testEmptyStringError() {
        // Given
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError("");

        // Then
        assertEquals("", response.getError());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void testWhitespaceError() {
        // Given
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError("   ");

        // Then
        assertEquals("   ", response.getError());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void testErrorWithSpecialCharacters() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with special characters: @#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithUnicodeCharacters() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Erreur avec des caractères spéciaux: ñáéíóú";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithHTML() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with <script>alert('xss')</script> HTML content";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithNumbers() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error code: 500, Status: 12345, Time: 2023-12-25";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithMixedCase() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "ERROR WITH MIXED CASE: Internal Server Error";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithLongString() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "This is a very long error message that describes in detail what went wrong during the processing of the request. " +
                      "It provides comprehensive information about the specific error that occurred, including details about the context, " +
                      "the parameters that were involved, and suggestions for how to resolve the issue. The message is intentionally long " +
                      "to test the handling of large text content in error responses.";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().length() > 200);
    }

    @Test
    void testErrorWithNewlines() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with\nmultiple\nlines\nof text";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("\n"));
    }

    @Test
    void testErrorWithTabs() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with\t\t\ttabs\tin\ttext";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("\t"));
    }

    @Test
    void testErrorWithQuotes() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with \"quotes\" and 'single quotes'";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("\""));
        assertTrue(response.getError().contains("'"));
    }

    @Test
    void testErrorWithBackslashes() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with \\backslashes\\ in \\text\\";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("\\"));
    }

    @Test
    void testErrorWithEmojis() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error with emojis: 😀😃😄😁😆😅😂🤣";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
    }

    @Test
    void testErrorWithURLs() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error occurred while accessing https://api.example.com/v1/users?param=value#fragment";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("https://"));
    }

    @Test
    void testErrorWithJSON() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Error parsing JSON: {\"field\": \"value\", \"number\": 123, \"array\": [1, 2, 3]}";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("{"));
        assertTrue(response.getError().contains("}"));
    }

    @Test
    void testErrorWithSQL() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "SQL Error: SELECT * FROM users WHERE id = 123 AND status = 'active'";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("SELECT"));
    }

    @Test
    void testErrorWithStackTrace() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Exception occurred:\n" +
                      "java.lang.NullPointerException: null\n" +
                      "    at com.example.Service.method(Service.java:123)\n" +
                      "    at com.example.Controller.handle(Controller.java:456)";

        // When
        response.setError(error);

        // Then
        assertEquals(error, response.getError());
        assertTrue(response.getError().contains("NullPointerException"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        ErrorResponse response1 = new ErrorResponse();
        ErrorResponse response2 = new ErrorResponse();
        ErrorResponse response3 = new ErrorResponse();

        String error = "Internal server error";

        // When
        response1.setError(error);
        response2.setError(error);
        response3.setError("Different error");

        // Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        // Given
        ErrorResponse response1 = new ErrorResponse();
        ErrorResponse response2 = new ErrorResponse();

        // When
        response1.setError(null);
        response2.setError(null);

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testEqualsWithEmptyString() {
        // Given
        ErrorResponse response1 = new ErrorResponse();
        ErrorResponse response2 = new ErrorResponse();

        // When
        response1.setError("");
        response2.setError("");

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        ErrorResponse response = new ErrorResponse();
        String error = "Internal server error";

        // When
        response.setError(error);
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("ErrorResponse"));
        assertTrue(toString.contains("error="));
    }

    @Test
    void testToStringWithNull() {
        // Given
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError(null);
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("ErrorResponse"));
        assertTrue(toString.contains("error="));
    }

    @Test
    void testToStringWithEmptyString() {
        // Given
        ErrorResponse response = new ErrorResponse();

        // When
        response.setError("");
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("ErrorResponse"));
        assertTrue(toString.contains("error="));
    }

    @Test
    void testMultipleErrorResponses() {
        // Given
        ErrorResponse response1 = new ErrorResponse();
        ErrorResponse response2 = new ErrorResponse();
        ErrorResponse response3 = new ErrorResponse();

        // When
        response1.setError("Error 1");
        response2.setError("Error 2");
        response3.setError("Error 3");

        // Then
        assertEquals("Error 1", response1.getError());
        assertEquals("Error 2", response2.getError());
        assertEquals("Error 3", response3.getError());
        
        assertNotEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response2, response3);
    }
} 