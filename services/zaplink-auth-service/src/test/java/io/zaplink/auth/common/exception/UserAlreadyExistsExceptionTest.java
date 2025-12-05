package io.zaplink.auth.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.zaplink.auth.common.constants.ApiConstants;

/**
 * Comprehensive test suite for UserAlreadyExistsException.
 * Tests all constructors and inheritance behavior.
 */
@DisplayName("UserAlreadyExistsException Tests")
class UserAlreadyExistsExceptionTest {

    @Test @DisplayName("Constructor with message should set correct error code")
    void constructor_WithMessage_ShouldSetCorrectErrorCode() {
        // Given
        String message = "User already exists with email: test@example.com";
        
        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_USER_ALREADY_EXISTS, exception.getErrorCode());
        assertNotNull(exception);
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test @DisplayName("Constructor with message and cause should set all fields")
    void constructor_WithMessageAndCause_ShouldSetAllFields() {
        // Given
        String message = "User creation failed";
        Throwable cause = new RuntimeException("Constraint violation");
        
        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(ApiConstants.ERROR_USER_ALREADY_EXISTS, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test @DisplayName("Exception should maintain inheritance chain")
    void exception_ShouldMaintainInheritanceChain() {
        // Given
        String message = "Test user already exists";
        
        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);
        
        // Then
        assertTrue(exception instanceof UserAlreadyExistsException);
        assertTrue(exception instanceof AuthException);
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test @DisplayName("Exception with null message should handle gracefully")
    void exception_WithNullMessage_ShouldHandleGracefully() {
        // Given
        String message = null;
        
        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);
        
        // Then
        assertEquals(null, exception.getMessage());
        assertEquals(ApiConstants.ERROR_USER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test @DisplayName("Exception toString should include relevant information")
    void exceptionToString_ShouldIncludeRelevantInformation() {
        // Given
        String message = "User already exists: john.doe@example.com";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);
        
        // When
        String exceptionString = exception.toString();
        
        // Then
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("UserAlreadyExistsException"));
        assertTrue(exceptionString.contains(message));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }
}
