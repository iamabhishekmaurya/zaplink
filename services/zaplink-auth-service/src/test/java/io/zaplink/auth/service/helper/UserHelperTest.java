package io.zaplink.auth.service.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;

/**
 * Comprehensive test suite for UserHelper.
 * Tests all user utility operations and validation methods.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserHelper Tests")
class UserHelperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserHelper userHelper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .active(true)
                .verified(true)
                .build();
    }

    @Test
    @DisplayName("Find user by ID with existing user should return user")
    void findUserByIdOrThrow_ExistingUser_ReturnsUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(testUser));

        // When
        User result = userHelper.findUserByIdOrThrow(1L, "test operation");

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Find user by ID with non-existing user should throw UserNotFoundException")
    void findUserByIdOrThrow_NonExistingUser_ThrowsUserNotFoundException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(java.util.Optional.empty());
        try (var mockedStringUtil = mockStatic(io.zaplink.auth.common.util.StringUtil.class)) {
            mockedStringUtil.when(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with ID: "), eq(999L)))
                    .thenReturn("User not found for ID: 999");

            // When & Then
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userHelper.findUserByIdOrThrow(999L, "test operation")
            );

            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("999"));
            verify(userRepository).findById(999L);
            mockedStringUtil.verify(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with ID: "), eq(999L)));
        }
    }

    @Test
    @DisplayName("Find user by email with existing user should return user")
    void findUserByEmailOrThrow_ExistingUser_ReturnsUser() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(testUser));

        // When
        User result = userHelper.findUserByEmailOrThrow("test@example.com", "test operation");

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Find user by email with non-existing user should throw UserNotFoundException")
    void findUserByEmailOrThrow_NonExistingUser_ThrowsUserNotFoundException() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());
        try (var mockedStringUtil = mockStatic(io.zaplink.auth.common.util.StringUtil.class)) {
            mockedStringUtil.when(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with email: "), eq("nonexistent@example.com")))
                    .thenReturn("User not found for email: nonexistent@example.com");

            // When & Then
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userHelper.findUserByEmailOrThrow("nonexistent@example.com", "test operation")
            );

            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("nonexistent@example.com"));
            verify(userRepository).findByEmail("nonexistent@example.com");
            mockedStringUtil.verify(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with email: "), eq("nonexistent@example.com")));
        }
    }

    @Test
    @DisplayName("Update user with valid updater should save and return user")
    void updateUser_ValidUpdater_SavesAndReturnsUser() {
        // Given
        when(userRepository.save(testUser)).thenReturn(testUser);
        Consumer<User> updater = user -> {
            user.setFirstName("Updated");
            user.setLastName("Name");
        };

        // When
        User result = userHelper.updateUser(testUser, updater);

        // Then
        assertNotNull(result);
        assertEquals("Updated", testUser.getFirstName());
        assertEquals("Name", testUser.getLastName());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Update user with null updater should throw exception")
    void updateUser_NullUpdater_ThrowsException() {
        // When & Then
        assertThrows(NullPointerException.class,
                () -> userHelper.updateUser(testUser, null));
    }

    @Test
    @DisplayName("Update user should apply multiple updates")
    void updateUser_MultipleUpdates_AppliesAllUpdates() {
        // Given
        when(userRepository.save(testUser)).thenReturn(testUser);
        Consumer<User> updater = user -> {
            user.setFirstName("Updated");
            user.setLastName("Name");
            user.setPhoneNumber("1234567890");
        };

        // When
        User result = userHelper.updateUser(testUser, updater);

        // Then
        assertNotNull(result);
        assertEquals("Updated", testUser.getFirstName());
        assertEquals("Name", testUser.getLastName());
        assertEquals("1234567890", testUser.getPhoneNumber());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Validate active user with active user should succeed")
    void validateUserActive_ActiveUser_Succeeds() {
        // Given
        testUser.setActive(true);

        // When & Then
        assertDoesNotThrow(() -> userHelper.validateUserActive(testUser, "test operation"));
    }

    @Test
    @DisplayName("Validate active user with inactive user should throw IllegalArgumentException")
    void validateUserActive_InactiveUser_ThrowsIllegalArgumentException() {
        // Given
        testUser.setActive(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userHelper.validateUserActive(testUser, "test operation")
        );

        assertNotNull(exception);
        assertEquals(ApiConstants.MESSAGE_USER_ACCOUNT_DEACTIVATED, exception.getMessage());
    }

    @Test
    @DisplayName("Validate active user with null user should throw exception")
    void validateUserActive_NullUser_ThrowsException() {
        // When & Then
        assertThrows(NullPointerException.class,
                () -> userHelper.validateUserActive(null, "test operation"));
    }

    @Test
    @DisplayName("Find user by ID with null ID should handle gracefully")
    void findUserByIdOrThrow_NullId_HandlesGracefully() {
        // Given
        when(userRepository.findById(null)).thenReturn(java.util.Optional.empty());
        try (var mockedStringUtil = mockStatic(io.zaplink.auth.common.util.StringUtil.class)) {
            mockedStringUtil.when(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with ID: "), isNull()))
                    .thenReturn("User not found for ID: null");

            // When & Then
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userHelper.findUserByIdOrThrow(null, "test operation")
            );

            assertNotNull(exception);
            verify(userRepository).findById(null);
            mockedStringUtil.verify(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with ID: "), isNull()));
        }
    }

    @Test
    @DisplayName("Find user by email with null email should handle gracefully")
    void findUserByEmailOrThrow_NullEmail_HandlesGracefully() {
        // Given
        when(userRepository.findByEmail(null)).thenReturn(java.util.Optional.empty());
        try (var mockedStringUtil = mockStatic(io.zaplink.auth.common.util.StringUtil.class)) {
            mockedStringUtil.when(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with email: "), isNull()))
                    .thenReturn("User not found for email: null");

            // When & Then
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userHelper.findUserByEmailOrThrow(null, "test operation")
            );

            assertNotNull(exception);
            verify(userRepository).findByEmail(null);
            mockedStringUtil.verify(() -> io.zaplink.auth.common.util.StringUtil.appendValue(eq("User not found with email: "), isNull()));
        }
    }

    @Test
    @DisplayName("Update user with null user should throw exception")
    void updateUser_NullUser_ThrowsException() {
        // Given
        Consumer<User> updater = user -> user.setFirstName("Updated");

        // When & Then
        assertThrows(NullPointerException.class,
                () -> userHelper.updateUser(null, updater));
    }

    @Test
    @DisplayName("Update user should handle repository exception")
    void updateUser_RepositoryException_PropagatesException() {
        // Given
        when(userRepository.save(testUser)).thenThrow(new RuntimeException("Database error"));
        Consumer<User> updater = user -> user.setFirstName("Updated");

        // When & Then
        assertThrows(RuntimeException.class,
                () -> userHelper.updateUser(testUser, updater));

        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Validate active user with different operation names should work")
    void validateUserActive_DifferentOperations_WorkCorrectly() {
        // Given
        testUser.setActive(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> userHelper.validateUserActive(testUser, "login"));
        assertThrows(IllegalArgumentException.class,
                () -> userHelper.validateUserActive(testUser, "authentication"));
        assertThrows(IllegalArgumentException.class,
                () -> userHelper.validateUserActive(testUser, "password reset"));
    }

    @Test
    @DisplayName("Update user with no-op updater should still save")
    void updateUser_NoOpUpdater_StillSaves() {
        // Given
        when(userRepository.save(testUser)).thenReturn(testUser);
        Consumer<User> updater = user -> {
            // No changes
        };

        // When
        User result = userHelper.updateUser(testUser, updater);

        // Then
        assertNotNull(result);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Find user by ID with repository exception should propagate")
    void findUserByIdOrThrow_RepositoryException_PropagatesException() {
        // Given
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> userHelper.findUserByIdOrThrow(1L, "test operation"));

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Find user by email with repository exception should propagate")
    void findUserByEmailOrThrow_RepositoryException_PropagatesException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> userHelper.findUserByEmailOrThrow("test@example.com", "test operation"));

        verify(userRepository).findByEmail("test@example.com");
    }
}
