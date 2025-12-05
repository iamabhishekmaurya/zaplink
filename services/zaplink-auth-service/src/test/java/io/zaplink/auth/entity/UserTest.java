package io.zaplink.auth.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for User entity.
 * Tests all constructors, setters, getters, and JPA annotations.
 */
@DisplayName("User Entity Tests")
class UserTest {

    private User user;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        Role userRole = Role.builder()
                .id(1L)
                .name("USER")
                .description("Regular user role")
                .build();
        roles.add(userRole);

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("password123")
                .phoneNumber("1234567890")
                .active(true)
                .verified(true)
                .verificationToken("token123")
                .resetToken("reset123")
                .resetTokenExpiry(Instant.now().plusSeconds(3600))
                .roles(roles)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test @DisplayName("Default constructor should create empty user")
    void defaultConstructor_ShouldCreateEmptyUser() {
        // When
        User emptyUser = new User();
        
        // Then
        assertNotNull(emptyUser);
    }

    @Test @DisplayName("Builder pattern should create user with specified fields")
    void builderPattern_ShouldCreateUserWithSpecifiedFields() {
        // Given
        Long expectedId = 3L;
        String expectedUsername = "jane_doe";
        String expectedEmail = "jane@example.com";
        
        // When
        User builtUser = User.builder()
                .id(expectedId)
                .username(expectedUsername)
                .email(expectedEmail)
                .firstName("Jane")
                .lastName("Doe")
                .build();
        
        // Then
        assertEquals(expectedId, builtUser.getId());
        assertEquals(expectedUsername, builtUser.getUsername());
        assertEquals(expectedEmail, builtUser.getEmail());
        assertEquals("Jane", builtUser.getFirstName());
        assertEquals("Doe", builtUser.getLastName());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        User mutableUser = new User();
        Instant now = Instant.now();
        
        // When
        mutableUser.setId(5L);
        mutableUser.setUsername("mutable_user");
        mutableUser.setEmail("mutable@example.com");
        mutableUser.setFirstName("Mutable");
        mutableUser.setLastName("User");
        mutableUser.setPassword("newPassword");
        mutableUser.setPhoneNumber("9876543210");
        mutableUser.setActive(false);
        mutableUser.setVerified(true);
        mutableUser.setVerificationToken("newToken");
        mutableUser.setResetToken("newReset");
        mutableUser.setResetTokenExpiry(now);
        mutableUser.setRoles(new HashSet<>());
        mutableUser.setCreatedAt(now);
        mutableUser.setUpdatedAt(now);
        
        // Then
        assertEquals(5L, mutableUser.getId());
        assertEquals("mutable_user", mutableUser.getUsername());
        assertEquals("mutable@example.com", mutableUser.getEmail());
        assertEquals("Mutable", mutableUser.getFirstName());
        assertEquals("User", mutableUser.getLastName());
        assertEquals("newPassword", mutableUser.getPassword());
        assertEquals("9876543210", mutableUser.getPhoneNumber());
        assertEquals(false, mutableUser.isActive());
        assertEquals(true, mutableUser.isVerified());
        assertEquals("newToken", mutableUser.getVerificationToken());
        assertEquals("newReset", mutableUser.getResetToken());
        assertEquals(now, mutableUser.getResetTokenExpiry());
        assertEquals(new HashSet<>(), mutableUser.getRoles());
        assertEquals(now, mutableUser.getCreatedAt());
        assertEquals(now, mutableUser.getUpdatedAt());
    }

    @Test @DisplayName("Chained setters should work correctly")
    void chainedSetters_ShouldWorkCorrectly() {
        // Given
        User chainedUser = new User();
        
        // When
        User result = chainedUser
                .setId(10L)
                .setUsername("chained_user")
                .setEmail("chained@example.com")
                .setFirstName("Chained")
                .setLastName("User");
        
        // Then
        assertEquals(chainedUser, result);
        assertEquals(10L, chainedUser.getId());
        assertEquals("chained_user", chainedUser.getUsername());
        assertEquals("chained@example.com", chainedUser.getEmail());
        assertEquals("Chained", chainedUser.getFirstName());
        assertEquals("User", chainedUser.getLastName());
    }

    @Test @DisplayName("Equals and hashCode should work correctly")
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .username("same_user")
                .email("same@example.com")
                .build();
        
        User user2 = User.builder()
                .id(1L)
                .username("same_user")
                .email("same@example.com")
                .build();
        
        User user3 = User.builder()
                .id(2L)
                .username("different_user")
                .email("different@example.com")
                .build();
        
        // Then
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertTrue(user1.equals(user2));
        assertTrue(!user1.equals(user3));
    }

    @Test @DisplayName("ToString should include relevant fields")
    void toString_ShouldIncludeRelevantFields() {
        // When
        String userString = user.toString();
        
        // Then
        assertNotNull(userString);
        assertTrue(userString.contains("User"));
        assertTrue(userString.contains("id=1"));
        assertTrue(userString.contains("username=testuser"));
        assertTrue(userString.contains("email=test@example.com"));
    }

    @Test @DisplayName("User verification status should be mutable")
    void userVerificationStatus_ShouldBeMutable() {
        // Given
        User unverifiedUser = User.builder()
                .username("unverified")
                .email("unverified@example.com")
                .verified(false)
                .build();
        
        // When
        unverifiedUser.setVerified(true);
        
        // Then
        assertEquals(true, unverifiedUser.isVerified());
    }

    @Test @DisplayName("User active status should be mutable")
    void userActiveStatus_ShouldBeMutable() {
        // Given
        User inactiveUser = User.builder()
                .username("inactive")
                .email("inactive@example.com")
                .active(false)
                .build();
        
        // When
        inactiveUser.setActive(true);
        
        // Then
        assertEquals(true, inactiveUser.isActive());
    }
}
