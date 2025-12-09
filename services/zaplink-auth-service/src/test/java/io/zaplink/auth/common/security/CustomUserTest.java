package io.zaplink.auth.common.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;

/**
 * Comprehensive test suite for CustomUser.
 * Tests user loading and validation functionality.
 */
@DisplayName("CustomUser Tests")
class CustomUserTest {

    private CustomUser customUser;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUser = new CustomUser(userRepository);
    }

    @Test @DisplayName("Load user by username should return UserDetails when user exists and is active")
    void loadUserByUsername_ShouldReturnUserDetailsWhenUserExistsAndIsActive() {
        // Given
        String email = "test@example.com";
        String password = "encodedPassword";
        
        when(mockUser.getEmail()).thenReturn(email);
        when(mockUser.getPassword()).thenReturn(password);
        when(mockUser.isActive()).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        
        // When
        UserDetails userDetails = customUser.loadUserByUsername(email);
        
        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should throw exception when user not found")
    void loadUserByUsername_ShouldThrowExceptionWhenUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            customUser.loadUserByUsername(email);
        });
        
        assertTrue(exception.getMessage().contains("User not found for email: " + email));
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should throw exception when user is inactive")
    void loadUserByUsername_ShouldThrowExceptionWhenUserIsInactive() {
        // Given
        String email = "inactive@example.com";
        
        when(mockUser.getEmail()).thenReturn(email);
        when(mockUser.isActive()).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            customUser.loadUserByUsername(email);
        });
        
        assertTrue(exception.getMessage().contains(ApiConstants.MESSAGE_USER_ACCOUNT_DEACTIVATED_EMAIL));
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should handle null email")
    void loadUserByUsername_ShouldHandleNullEmail() {
        // Given
        String email = null;
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            customUser.loadUserByUsername(email);
        });
        
        assertTrue(exception.getMessage().contains("User not found for email: "));
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should handle empty email")
    void loadUserByUsername_ShouldHandleEmptyEmail() {
        // Given
        String email = "";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            customUser.loadUserByUsername(email);
        });
        
        assertTrue(exception.getMessage().contains("User not found for email: "));
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should handle repository exception")
    void loadUserByUsername_ShouldHandleRepositoryException() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenThrow(new RuntimeException("Database error"));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customUser.loadUserByUsername(email);
        });
        
        verify(userRepository).findByEmail(email);
    }

    @Test @DisplayName("Load user by username should work with multiple calls")
    void loadUserByUsername_ShouldWorkWithMultipleCalls() {
        // Given
        String email1 = "user1@example.com";
        String email2 = "user2@example.com";
        
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        
        when(mockUser1.getEmail()).thenReturn(email1);
        when(mockUser1.getPassword()).thenReturn("password1");
        when(mockUser1.isActive()).thenReturn(true);
        
        when(mockUser2.getEmail()).thenReturn(email2);
        when(mockUser2.getPassword()).thenReturn("password2");
        when(mockUser2.isActive()).thenReturn(true);
        
        when(userRepository.findByEmail(email1)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByEmail(email2)).thenReturn(Optional.of(mockUser2));
        
        // When
        UserDetails userDetails1 = customUser.loadUserByUsername(email1);
        UserDetails userDetails2 = customUser.loadUserByUsername(email2);
        
        // Then
        assertEquals(email1, userDetails1.getUsername());
        assertEquals(email2, userDetails2.getUsername());
        
        verify(userRepository).findByEmail(email1);
        verify(userRepository).findByEmail(email2);
    }
}
