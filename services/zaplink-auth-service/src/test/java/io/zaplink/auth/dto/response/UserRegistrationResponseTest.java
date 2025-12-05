package io.zaplink.auth.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for UserRegistrationResponse.
 * Tests all constructors, builder patterns, and inheritance from BaseResponse.
 */
@DisplayName("UserRegistrationResponse Tests")
class UserRegistrationResponseTest {

    @Test @DisplayName("Default constructor should create empty response")
    void defaultConstructor_ShouldCreateEmptyResponse() {
        // When
        UserRegistrationResponse response = new UserRegistrationResponse();
        
        // Then
        assertNotNull(response);
    }

    @Test @DisplayName("Builder pattern should create response with all fields")
    void builderPattern_ShouldCreateResponseWithAllFields() {
        // Given
        Instant now = Instant.now();
        
        // When
        UserRegistrationResponse response = UserRegistrationResponse.builder()
                .success(true)
                .message("User registered successfully")
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .verified(false)
                .createdAt(now)
                .build();
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertFalse(response.isVerified());
        assertEquals(now, response.getCreatedAt());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        UserRegistrationResponse response = new UserRegistrationResponse();
        Instant now = Instant.now();
        
        // When
        response.setSuccess(true);
        response.setMessage("Updated message");
        response.setUserId(2L);
        response.setUsername("user2");
        response.setEmail("user2@example.com");
        response.setFirstName("User");
        response.setLastName("Two");
        response.setVerified(true);
        response.setCreatedAt(now);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals(2L, response.getUserId());
        assertEquals("user2", response.getUsername());
        assertEquals("user2@example.com", response.getEmail());
        assertEquals("User", response.getFirstName());
        assertEquals("Two", response.getLastName());
        assertTrue(response.isVerified());
        assertEquals(now, response.getCreatedAt());
    }

    @Test @DisplayName("Builder with partial fields should work")
    void builderWithPartialFields_ShouldWork() {
        // When
        UserRegistrationResponse response = UserRegistrationResponse.builder()
                .userId(3L)
                .email("user3@example.com")
                .verified(true)
                .build();
        
        // Then
        assertEquals(3L, response.getUserId());
        assertEquals("user3@example.com", response.getEmail());
        assertTrue(response.isVerified());
    }

    @Test @DisplayName("Inheritance from BaseResponse should work")
    void inheritanceFromBaseResponse_ShouldWork() {
        // Given
        String errorMessage = "Registration failed";
        String errorCode = "REG001";
        
        // When
        UserRegistrationResponse response = UserRegistrationResponse.builder()
                .success(false)
                .message(errorMessage)
                .errorCode(errorCode)
                .userId(null)
                .build();
        
        // Then
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(null, response.getUserId());
    }
}
