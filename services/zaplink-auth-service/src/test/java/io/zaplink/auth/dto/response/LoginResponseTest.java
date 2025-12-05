package io.zaplink.auth.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for LoginResponse.
 * Tests all constructors, builder patterns, and nested UserInfo class.
 */
@DisplayName("LoginResponse Tests")
class LoginResponseTest {

    @Test @DisplayName("Default constructor should create empty response")
    void defaultConstructor_ShouldCreateEmptyResponse() {
        // When
        LoginResponse response = new LoginResponse();
        
        // Then
        assertNotNull(response);
        assertEquals("Bearer", response.getTokenType());
    }

    @Test @DisplayName("Builder pattern should create response with all fields")
    void builderPattern_ShouldCreateResponseWithAllFields() {
        // Given
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(1L);
        userInfo.setUsername("testuser");
        userInfo.setEmail("test@example.com");
        userInfo.setFirstName("Test");
        userInfo.setLastName("User");
        userInfo.setVerified(true);
        
        // When
        LoginResponse response = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .expiresIn(1800L)
                .userInfo(userInfo)
                .build();
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1800L, response.getExpiresIn());
        assertEquals(userInfo, response.getUserInfo());
        assertEquals(1L, response.getUserInfo().getId());
        assertEquals("testuser", response.getUserInfo().getUsername());
        assertEquals("test@example.com", response.getUserInfo().getEmail());
        assertEquals("Test", response.getUserInfo().getFirstName());
        assertEquals("User", response.getUserInfo().getLastName());
        assertTrue(response.getUserInfo().isVerified());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        LoginResponse response = new LoginResponse();
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        
        // When
        response.setSuccess(true);
        response.setMessage("Updated message");
        response.setAccessToken("new-access-token");
        response.setRefreshToken("new-refresh-token");
        response.setExpiresIn(7200L);
        response.setUserInfo(userInfo);
        
        // Then
        assertTrue(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(7200L, response.getExpiresIn());
        assertEquals(userInfo, response.getUserInfo());
    }

    @Test @DisplayName("UserInfo default constructor should work")
    void userInfoDefaultConstructor_ShouldWork() {
        // When
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        
        // Then
        assertNotNull(userInfo);
    }

    @Test @DisplayName("UserInfo allArgsConstructor should work")
    void userInfoAllArgsConstructor_ShouldWork() {
        // Given
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        String firstName = "Test";
        String lastName = "User";
        boolean verified = true;
        
        // When
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                id, username, email, firstName, lastName, verified);
        
        // Then
        assertEquals(id, userInfo.getId());
        assertEquals(username, userInfo.getUsername());
        assertEquals(email, userInfo.getEmail());
        assertEquals(firstName, userInfo.getFirstName());
        assertEquals(lastName, userInfo.getLastName());
        assertEquals(verified, userInfo.isVerified());
    }

    @Test @DisplayName("UserInfo setters and getters should work")
    void userInfoSettersAndGetters_ShouldWork() {
        // Given
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        
        // When
        userInfo.setId(3L);
        userInfo.setUsername("user3");
        userInfo.setEmail("user3@example.com");
        userInfo.setFirstName("Three");
        userInfo.setLastName("User");
        userInfo.setVerified(true);
        
        // Then
        assertEquals(3L, userInfo.getId());
        assertEquals("user3", userInfo.getUsername());
        assertEquals("user3@example.com", userInfo.getEmail());
        assertEquals("Three", userInfo.getFirstName());
        assertEquals("User", userInfo.getLastName());
        assertTrue(userInfo.isVerified());
    }
}
