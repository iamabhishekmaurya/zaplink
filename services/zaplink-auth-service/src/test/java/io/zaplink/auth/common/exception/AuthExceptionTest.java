package io.zaplink.auth.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.zaplink.auth.common.constants.ApiConstants;

/**
 * Comprehensive test suite for AuthException.
 * Tests all constructors and error code handling.
 */
@DisplayName("AuthException Tests")
class AuthExceptionTest {

    @Test @DisplayName("Constructor with message should set default error code")
    void constructor_WithMessage_ShouldSetDefaultErrorCode() {
        // Given
        String message = "Authentication failed";
        
        // When
        AuthException exception = new AuthException(message);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_AUTH_ERROR, exception.getErrorCode());
        assertNull(exception.getCause());
    }

    @Test @DisplayName("Constructor with message and error code should set custom error code")
    void constructor_WithMessageAndErrorCode_ShouldSetCustomErrorCode() {
        // Given
        String message = "Invalid credentials";
        String errorCode = "INVALID_CREDENTIALS";
        
        // When
        AuthException exception = new AuthException(message, errorCode);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertNull(exception.getCause());
    }

    @Test @DisplayName("Constructor with message and cause should set default error code")
    void constructor_WithMessageAndCause_ShouldSetDefaultErrorCode() {
        // Given
        String message = "Authentication error";
        Throwable cause = new RuntimeException("Root cause");
        
        // When
        AuthException exception = new AuthException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_AUTH_ERROR, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test @DisplayName("Constructor with message, error code, and cause should set all fields")
    void constructor_WithMessageErrorCodeAndCause_ShouldSetAllFields() {
        // Given
        String message = "Custom auth error";
        String errorCode = "CUSTOM_ERROR";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        
        // When
        AuthException exception = new AuthException(message, errorCode, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test @DisplayName("GetErrorCode should return correct error code")
    void getErrorCode_ShouldReturnCorrectErrorCode() {
        // Given
        String expectedErrorCode = "TEST_ERROR";
        AuthException exception = new AuthException("Test message", expectedErrorCode);
        
        // When
        String actualErrorCode = exception.getErrorCode();
        
        // Then
        assertEquals(expectedErrorCode, actualErrorCode);
    }

    @Test @DisplayName("Exception should maintain RuntimeException behavior")
    void exception_ShouldMaintainRuntimeExceptionBehavior() {
        // Given
        String message = "Test exception";
        AuthException exception = new AuthException(message);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
        assertEquals(message, exception.getMessage());
    }

    @Test @DisplayName("Exception with null message should handle gracefully")
    void exception_WithNullMessage_ShouldHandleGracefully() {
        // Given
        String message = null;
        
        // When
        AuthException exception = new AuthException(message);
        
        // Then
        assertNull(exception.getMessage());
        assertEquals(ApiConstants.ERROR_AUTH_ERROR, exception.getErrorCode());
    }

    @Test @DisplayName("Exception with null error code should handle gracefully")
    void exception_WithNullErrorCode_ShouldHandleGracefully() {
        // Given
        String message = "Test message";
        String errorCode = null;
        
        // When
        AuthException exception = new AuthException(message, errorCode);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
    }

    @Test @DisplayName("Exception toString should include relevant information")
    void exceptionToString_ShouldIncludeRelevantInformation() {
        // Given
        String message = "Test auth exception";
        String errorCode = "TEST_CODE";
        AuthException exception = new AuthException(message, errorCode);
        
        // When
        String exceptionString = exception.toString();
        
        // Then
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("AuthException"));
        assertTrue(exceptionString.contains(message));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }
}
