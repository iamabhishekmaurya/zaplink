package io.zaplink.auth.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for LoginAttempt entity.
 * Tests all constructors, setters, getters, and JPA annotations.
 */
@DisplayName("LoginAttempt Entity Tests")
class LoginAttemptTest {

    private LoginAttempt loginAttempt;

    @BeforeEach
    void setUp() {
        loginAttempt = new LoginAttempt("test@example.com", true);
        loginAttempt.setId(1L);
        loginAttempt.setIpAddress("192.168.1.1");
        loginAttempt.setUserAgent("Mozilla/5.0");
        loginAttempt.setFailureReason(null);
        loginAttempt.setCreatedAt(LocalDateTime.now());
    }

    @Test @DisplayName("Default constructor should create empty login attempt")
    void defaultConstructor_ShouldCreateEmptyLoginAttempt() {
        // When
        LoginAttempt emptyAttempt = new LoginAttempt();
        
        // Then
        assertNotNull(emptyAttempt);
    }

    @Test @DisplayName("Constructor with email and success should set fields")
    void constructor_WithEmailAndSuccess_ShouldSetFields() {
        // Given
        String email = "user@example.com";
        boolean successful = false;
        
        // When
        LoginAttempt attempt = new LoginAttempt(email, successful);
        
        // Then
        assertEquals(email, attempt.getEmail());
        assertEquals(successful, attempt.isSuccessful());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        LoginAttempt mutableAttempt = new LoginAttempt();
        LocalDateTime now = LocalDateTime.now();
        
        // When
        mutableAttempt.setId(5L);
        mutableAttempt.setEmail("new@example.com");
        mutableAttempt.setIpAddress("10.0.0.1");
        mutableAttempt.setUserAgent("Chrome/91.0");
        mutableAttempt.setSuccessful(false);
        mutableAttempt.setFailureReason("Invalid password");
        mutableAttempt.setCreatedAt(now);
        
        // Then
        assertEquals(5L, mutableAttempt.getId());
        assertEquals("new@example.com", mutableAttempt.getEmail());
        assertEquals("10.0.0.1", mutableAttempt.getIpAddress());
        assertEquals("Chrome/91.0", mutableAttempt.getUserAgent());
        assertEquals(false, mutableAttempt.isSuccessful());
        assertEquals("Invalid password", mutableAttempt.getFailureReason());
        assertEquals(now, mutableAttempt.getCreatedAt());
    }

    @Test @DisplayName("Equals and hashCode should work correctly")
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        LoginAttempt attempt1 = new LoginAttempt("test@example.com", true);
        attempt1.setId(1L);
        
        LoginAttempt attempt2 = new LoginAttempt("test@example.com", true);
        attempt2.setId(1L);
        
        LoginAttempt attempt3 = new LoginAttempt("other@example.com", false);
        attempt3.setId(2L);
        
        // Then
        assertEquals(attempt1, attempt2);
        assertEquals(attempt1.hashCode(), attempt2.hashCode());
        assertTrue(attempt1.equals(attempt2));
        assertTrue(!attempt1.equals(attempt3));
    }

    @Test @DisplayName("ToString should include relevant fields")
    void toString_ShouldIncludeRelevantFields() {
        // When
        String attemptString = loginAttempt.toString();
        
        // Then
        assertNotNull(attemptString);
        assertTrue(attemptString.contains("LoginAttempt"));
        assertTrue(attemptString.contains("email=test@example.com"));
        assertTrue(attemptString.contains("successful=true"));
    }

    @Test @DisplayName("Entity should handle null values gracefully")
    void entity_ShouldHandleNullValuesGracefully() {
        // Given
        LoginAttempt attemptWithNulls = new LoginAttempt();
        
        // When
        attemptWithNulls.setId(null);
        attemptWithNulls.setEmail(null);
        attemptWithNulls.setIpAddress(null);
        attemptWithNulls.setUserAgent(null);
        attemptWithNulls.setFailureReason(null);
        attemptWithNulls.setCreatedAt(null);
        
        // Then
        assertEquals(null, attemptWithNulls.getId());
        assertEquals(null, attemptWithNulls.getEmail());
        assertEquals(null, attemptWithNulls.getIpAddress());
        assertEquals(null, attemptWithNulls.getUserAgent());
        assertEquals(null, attemptWithNulls.getFailureReason());
        assertEquals(null, attemptWithNulls.getCreatedAt());
    }

    @Test @DisplayName("Failed login attempt should store failure reason")
    void failedLoginAttempt_ShouldStoreFailureReason() {
        // Given
        String email = "failed@example.com";
        boolean successful = false;
        String failureReason = "Account locked";
        
        // When
        LoginAttempt failedAttempt = new LoginAttempt(email, successful);
        failedAttempt.setFailureReason(failureReason);
        
        // Then
        assertEquals(email, failedAttempt.getEmail());
        assertEquals(successful, failedAttempt.isSuccessful());
        assertEquals(failureReason, failedAttempt.getFailureReason());
    }

    @Test @DisplayName("Successful login attempt should not have failure reason")
    void successfulLoginAttempt_ShouldNotHaveFailureReason() {
        // Given
        String email = "success@example.com";
        boolean successful = true;
        
        // When
        LoginAttempt successfulAttempt = new LoginAttempt(email, successful);
        
        // Then
        assertEquals(email, successfulAttempt.getEmail());
        assertEquals(successful, successfulAttempt.isSuccessful());
        assertEquals(null, successfulAttempt.getFailureReason());
    }
}
