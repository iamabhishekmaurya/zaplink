package io.zaplink.auth.common.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Utility class for common token operations.
 * Provides reusable methods for token generation and validation.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Component
public class TokenUtility
{
    // ==================== TIME VALUES ====================
    private static final long REFRESH_TOKEN_EXPIRY_DAYS   = 7;
    private static final long PASSWORD_RESET_EXPIRY_HOURS = 1;
    // ==================== TOKEN MASKING ====================
    private static final int    TOKEN_MASK_LENGTH           = 10;
    private static final String TOKEN_SUFFIX                = "...";
    private static final String FULL_TOKEN_MASK             = "***";
    /**
     * Generates a new UUID-based token string.
     * 
     * @return The generated token
     */
    public String generateToken()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a refresh token expiry time.
     * 
     * @return The expiry time for refresh token
     */
    public Instant generateRefreshTokenExpiry()
    {
        return Instant.now().plus( REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS );
    }

    /**
     * Generates a password reset token expiry time.
     * 
     * @return The expiry time for password reset token
     */
    public Instant generatePasswordResetExpiry()
    {
        return Instant.now().plus( PASSWORD_RESET_EXPIRY_HOURS, ChronoUnit.HOURS );
    }

    /**
     * Masks a token for logging purposes.
     * Shows only the first few characters followed by a suffix.
     * 
     * @param token The token to mask
     * @return The masked token
     */
    public String maskToken( String token )
    {
        if ( token == null || token.length() <= TOKEN_MASK_LENGTH )
        {
            return FULL_TOKEN_MASK;
        }
        return token.substring( 0, TOKEN_MASK_LENGTH ) + TOKEN_SUFFIX;
    }

    /**
     * Validates if a token is expired.
     * 
     * @param expiryDate The expiry date to check
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired( Instant expiryDate )
    {
        return expiryDate.isBefore( Instant.now() );
    }
}
