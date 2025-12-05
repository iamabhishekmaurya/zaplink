package io.zaplink.auth.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.zaplink.auth.common.constants.SecurityConstants;

/**
 * Comprehensive test suite for TokenUtility.
 * Tests all token generation, validation, and masking operations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TokenUtility Tests")
class TokenUtilityTest {

    private TokenUtility tokenUtility;

    @BeforeEach
    void setUp() {
        tokenUtility = new TokenUtility();
    }

    @Test @DisplayName("generateToken should return valid UUID")
    void generateToken_ShouldReturnValidUUID() {
        // When
        String token = tokenUtility.generateToken();
        
        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify it's a valid UUID
        assertDoesNotThrow(() -> UUID.fromString(token));
    }

    @Test @DisplayName("generateToken should return different tokens on multiple calls")
    void generateToken_ShouldReturnDifferentTokensOnMultipleCalls() {
        // When
        String token1 = tokenUtility.generateToken();
        String token2 = tokenUtility.generateToken();
        
        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token1.equals(token2));
    }

    @Test @DisplayName("generateRefreshTokenExpiry should return future date")
    void generateRefreshTokenExpiry_ShouldReturnFutureDate() {
        // When
        Instant expiry = tokenUtility.generateRefreshTokenExpiry();
        
        // Then
        assertNotNull(expiry);
        assertTrue(expiry.isAfter(Instant.now()));
        
        // Verify it's exactly the configured number of days in the future (allowing small variance)
        Instant expectedExpiry = Instant.now().plus(SecurityConstants.REFRESH_TOKEN_EXPIRY_DAYS, java.time.temporal.ChronoUnit.DAYS);
        long difference = Math.abs(expiry.getEpochSecond() - expectedExpiry.getEpochSecond());
        assertTrue(difference < 60); // Allow 60 seconds variance
    }

    @Test @DisplayName("generatePasswordResetExpiry should return future date")
    void generatePasswordResetExpiry_ShouldReturnFutureDate() {
        // When
        Instant expiry = tokenUtility.generatePasswordResetExpiry();
        
        // Then
        assertNotNull(expiry);
        assertTrue(expiry.isAfter(Instant.now()));
        
        // Verify it's exactly the configured number of hours in the future (allowing small variance)
        Instant expectedExpiry = Instant.now().plus(SecurityConstants.PASSWORD_RESET_EXPIRY_HOURS, java.time.temporal.ChronoUnit.HOURS);
        long difference = Math.abs(expiry.getEpochSecond() - expectedExpiry.getEpochSecond());
        assertTrue(difference < 60); // Allow 60 seconds variance
    }

    @Test @DisplayName("maskToken should mask valid token")
    void maskToken_ShouldMaskValidToken() {
        // Given
        String token = "12345678-1234-1234-1234-123456789012";
        
        // When
        String maskedToken = tokenUtility.maskToken(token);
        
        // Then
        assertNotNull(maskedToken);
        assertFalse(maskedToken.equals(token));
        assertTrue(maskedToken.endsWith(SecurityConstants.TOKEN_SUFFIX));
        assertEquals(SecurityConstants.TOKEN_MASK_LENGTH + SecurityConstants.TOKEN_SUFFIX.length(), maskedToken.length());
    }

    @Test @DisplayName("maskToken should return full mask for null token")
    void maskToken_ShouldReturnFullMaskForNullToken() {
        // Given
        String token = null;
        
        // When
        String maskedToken = tokenUtility.maskToken(token);
        
        // Then
        assertEquals(SecurityConstants.FULL_TOKEN_MASK, maskedToken);
    }

    @Test @DisplayName("maskToken should return full mask for short token")
    void maskToken_ShouldReturnFullMaskForShortToken() {
        // Given
        String token = "123"; // Shorter than TOKEN_MASK_LENGTH
        
        // When
        String maskedToken = tokenUtility.maskToken(token);
        
        // Then
        assertEquals(SecurityConstants.FULL_TOKEN_MASK, maskedToken);
    }

    @Test @DisplayName("maskToken should handle token equal to mask length")
    void maskToken_ShouldHandleTokenEqualToMaskLength() {
        // Given
        String token = "12345678"; // Exactly TOKEN_MASK_LENGTH characters
        
        // When
        String maskedToken = tokenUtility.maskToken(token);
        
        // Then
        assertEquals(SecurityConstants.FULL_TOKEN_MASK, maskedToken);
    }

    @Test @DisplayName("isTokenExpired should return false for future date")
    void isTokenExpired_ShouldReturnFalseForFutureDate() {
        // Given
        Instant futureDate = Instant.now().plus(1, java.time.temporal.ChronoUnit.HOURS);
        
        // When
        boolean isExpired = tokenUtility.isTokenExpired(futureDate);
        
        // Then
        assertFalse(isExpired);
    }

    @Test @DisplayName("isTokenExpired should return true for past date")
    void isTokenExpired_ShouldReturnTrueForPastDate() {
        // Given
        Instant pastDate = Instant.now().minus(1, java.time.temporal.ChronoUnit.HOURS);
        
        // When
        boolean isExpired = tokenUtility.isTokenExpired(pastDate);
        
        // Then
        assertTrue(isExpired);
    }

    @Test @DisplayName("isTokenExpired should return false for current time")
    void isTokenExpired_ShouldReturnFalseForCurrentTime() {
        // Given
        Instant now = Instant.now();
        
        // When
        boolean isExpired = tokenUtility.isTokenExpired(now);
        
        // Then
        assertFalse(isExpired); // Current time should not be considered expired
    }

    private void assertDoesNotThrow(Runnable runnable) {
        runnable.run();
    }
}
