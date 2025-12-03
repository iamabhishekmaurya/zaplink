package io.zaplink.auth.common.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.zaplink.auth.common.constants.SecurityConstants;

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
        return Instant.now().plus( SecurityConstants.REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS );
    }

    /**
     * Generates a password reset token expiry time.
     * 
     * @return The expiry time for password reset token
     */
    public Instant generatePasswordResetExpiry()
    {
        return Instant.now().plus( SecurityConstants.PASSWORD_RESET_EXPIRY_HOURS, ChronoUnit.HOURS );
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
        if ( token == null || token.length() <= SecurityConstants.TOKEN_MASK_LENGTH )
        {
            return SecurityConstants.FULL_TOKEN_MASK;
        }
        return StringUtil.createMaskedToken( token, SecurityConstants.TOKEN_MASK_LENGTH,
                                             SecurityConstants.TOKEN_SUFFIX );
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
