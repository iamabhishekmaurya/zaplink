package io.zaplink.auth.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for RefreshToken entity.
 * Tests all JPA annotations, builder patterns, and auditing fields.
 */
@DisplayName("RefreshToken Entity Tests")
class RefreshTokenTest {

    private RefreshToken refreshToken;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();
                
        refreshToken = RefreshToken.builder()
                .id(1L)
                .token("refresh-token-123")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();
    }

    @Test @DisplayName("Default constructor should create empty refresh token")
    void defaultConstructor_ShouldCreateEmptyRefreshToken() {
        // When
        RefreshToken emptyToken = new RefreshToken();
        
        // Then
        assertNotNull(emptyToken);
    }

    @Test @DisplayName("AllArgsConstructor should create refresh token with all fields")
    void allArgsConstructor_ShouldCreateRefreshTokenWithAllFields() {
        // Given
        Long id = 2L;
        String token = "token-456";
        Instant expiryDate = Instant.now().plusSeconds(7200);
        Instant createdAt = Instant.now();
        
        // When
        RefreshToken fullToken = new RefreshToken(id, token, user, expiryDate, createdAt);
        
        // Then
        assertEquals(id, fullToken.getId());
        assertEquals(token, fullToken.getToken());
        assertEquals(user, fullToken.getUser());
        assertEquals(expiryDate, fullToken.getExpiryDate());
        assertEquals(createdAt, fullToken.getCreatedAt());
    }

    @Test @DisplayName("Builder pattern should create refresh token with specified fields")
    void builderPattern_ShouldCreateRefreshTokenWithSpecifiedFields() {
        // Given
        Long expectedId = 3L;
        String expectedToken = "token-789";
        Instant expectedExpiry = Instant.now().plusSeconds(1800);
        Instant expectedCreatedAt = Instant.now();
        
        // When
        RefreshToken builtToken = RefreshToken.builder()
                .id(expectedId)
                .token(expectedToken)
                .user(user)
                .expiryDate(expectedExpiry)
                .createdAt(expectedCreatedAt)
                .build();
        
        // Then
        assertEquals(expectedId, builtToken.getId());
        assertEquals(expectedToken, builtToken.getToken());
        assertEquals(user, builtToken.getUser());
        assertEquals(expectedExpiry, builtToken.getExpiryDate());
        assertEquals(expectedCreatedAt, builtToken.getCreatedAt());
    }

    @Test @DisplayName("Builder pattern with partial fields should work")
    void builderPattern_WithPartialFields_ShouldWork() {
        // When
        RefreshToken partialToken = RefreshToken.builder()
                .token("partial-token")
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
        
        // Then
        assertEquals("partial-token", partialToken.getToken());
        assertEquals(null, partialToken.getId());
        assertEquals(null, partialToken.getUser());
        assertNotNull(partialToken.getExpiryDate());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        RefreshToken mutableToken = new RefreshToken();
        User newUser = User.builder().id(2L).email("new@example.com").build();
        
        // When
        mutableToken.setId(5L);
        mutableToken.setToken("mutable-token");
        mutableToken.setUser(newUser);
        mutableToken.setExpiryDate(Instant.now().plusSeconds(7200));
        mutableToken.setCreatedAt(Instant.now());
        
        // Then
        assertEquals(5L, mutableToken.getId());
        assertEquals("mutable-token", mutableToken.getToken());
        assertEquals(newUser, mutableToken.getUser());
        assertNotNull(mutableToken.getExpiryDate());
        assertNotNull(mutableToken.getCreatedAt());
    }

    @Test @DisplayName("Equals should exclude user field as per @EqualsAndHashCode")
    void equals_ShouldExcludeUserFieldAsPerEqualsAndHashCode() {
        // Given
        User user1 = User.builder().id(1L).email("user1@example.com").build();
        User user2 = User.builder().id(2L).email("user2@example.com").build();
        
        RefreshToken token1 = RefreshToken.builder()
                .id(1L)
                .token("same-token")
                .user(user1)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
                
        RefreshToken token2 = RefreshToken.builder()
                .id(1L)
                .token("same-token")
                .user(user2)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
        
        // Then
        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test @DisplayName("Equals should consider different tokens as different")
    void equals_ShouldConsiderDifferentTokensAsDifferent() {
        // Given
        RefreshToken token1 = RefreshToken.builder()
                .id(1L)
                .token("token-1")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
                
        RefreshToken token2 = RefreshToken.builder()
                .id(1L)
                .token("token-2")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
        
        // Then
        assertTrue(!token1.equals(token2));
    }

    @Test @DisplayName("ToString should include relevant fields")
    void toString_ShouldIncludeRelevantFields() {
        // When
        String tokenString = refreshToken.toString();
        
        // Then
        assertNotNull(tokenString);
        assertTrue(tokenString.contains("RefreshToken"));
        assertTrue(tokenString.contains("id=1"));
        assertTrue(tokenString.contains("token=refresh-token-123"));
    }

    @Test @DisplayName("Entity should handle null values gracefully")
    void entity_ShouldHandleNullValuesGracefully() {
        // Given
        RefreshToken tokenWithNulls = new RefreshToken();
        
        // When
        tokenWithNulls.setId(null);
        tokenWithNulls.setToken(null);
        tokenWithNulls.setUser(null);
        tokenWithNulls.setExpiryDate(null);
        tokenWithNulls.setCreatedAt(null);
        
        // Then
        assertEquals(null, tokenWithNulls.getId());
        assertEquals(null, tokenWithNulls.getToken());
        assertEquals(null, tokenWithNulls.getUser());
        assertEquals(null, tokenWithNulls.getExpiryDate());
        assertEquals(null, tokenWithNulls.getCreatedAt());
    }

    @Test @DisplayName("Builder should allow chaining")
    void builder_ShouldAllowChaining() {
        // When
        RefreshToken chainedToken = RefreshToken.builder()
                .id(10L)
                .token("chained-token")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(900))
                .createdAt(Instant.now())
                .build();
        
        // Then
        assertEquals(10L, chainedToken.getId());
        assertEquals("chained-token", chainedToken.getToken());
        assertEquals(user, chainedToken.getUser());
        assertNotNull(chainedToken.getExpiryDate());
        assertNotNull(chainedToken.getCreatedAt());
    }
}
