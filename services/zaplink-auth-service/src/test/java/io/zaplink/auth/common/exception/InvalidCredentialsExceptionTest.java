package io.zaplink.auth.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.zaplink.auth.common.constants.ApiConstants;

/**
 * Comprehensive test suite for InvalidCredentialsException.
 * Tests all constructors and inheritance behavior.
 */
@DisplayName("InvalidCredentialsException Tests")
class InvalidCredentialsExceptionTest {

    @Test @DisplayName("Constructor with message should set correct error code")
    void constructor_WithMessage_ShouldSetCorrectErrorCode() {
        // Given
        String message = "Invalid username or password";
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_INVALID_CREDENTIALS, exception.getErrorCode());
        assertNotNull(exception);
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test @DisplayName("Constructor with message and cause should set all fields")
    void constructor_WithMessageAndCause_ShouldSetAllFields() {
        // Given
        String message = "Authentication failed";
        Throwable cause = new RuntimeException("Database connection failed");
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_INVALID_CREDENTIALS, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test @DisplayName("Exception should maintain inheritance chain")
    void exception_ShouldMaintainInheritanceChain() {
        // Given
        String message = "Test invalid credentials";
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message);
        
        // Then
        assertTrue(exception instanceof InvalidCredentialsException);
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test @DisplayName("Exception with null message should handle gracefully")
    void exception_WithNullMessage_ShouldHandleGracefully() {
        // Given
        String message = null;
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message);
        
        // Then
        assertEquals(null, exception.getMessage());
        assertEquals(ApiConstants.ERROR_INVALID_CREDENTIALS, exception.getErrorCode());
    }

    @Test @DisplayName("Exception with null cause should handle gracefully")
    void exception_WithNullCause_ShouldHandleGracefully() {
        // Given
        String message = "Test message";
        Throwable cause = null;
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_INVALID_CREDENTIALS, exception.getErrorCode());
        assertEquals(null, exception.getCause());
    }

    @Test @DisplayName("Exception toString should include relevant information")
    void exceptionToString_ShouldIncludeRelevantInformation() {
        // Given
        String message = "Invalid login credentials";
        InvalidCredentialsException exception = new InvalidCredentialsException(message);
        
        // When
        String exceptionString = exception.toString();
        
        // Then
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("InvalidCredentialsException"));
        assertTrue(exceptionString.contains(message));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }
}
