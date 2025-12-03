package io.zaplink.auth.common.constants;

/**
 * Security-related constants for authentication, authorization, JWT, and roles.
 * Contains all security configuration and token management constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
public final class SecurityConstants
{
    // Prevent instantiation
    private SecurityConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== ROLES ====================
    public static final String ROLE_USER                   = "USER";
    public static final String ROLE_ADMIN                  = "ADMIN";
    public static final String DEFAULT_ROLE_DESCRIPTION    = "Standard user role";
    // ==================== TOKENS ====================
    public static final String BEARER_PREFIX               = "Bearer ";
    public static final String AUTH_HEADER                 = "Authorization";
    public static final int    BEARER_PREFIX_LENGTH        = 7;
    public static final int    TOKEN_MASK_LENGTH           = 10;
    public static final String TOKEN_SUFFIX                = "...";
    public static final String FULL_TOKEN_MASK             = "***";
    public static final String TOKEN_TYPE_BEARER           = "Bearer";
    // ==================== TIME VALUES ====================
    public static final long   REFRESH_TOKEN_EXPIRY_DAYS   = 7;
    public static final long   PASSWORD_RESET_EXPIRY_HOURS = 1;
    // ==================== JWT CLAIMS ====================
    public static final String JWT_CLAIM_USER_ID           = "userId";
    public static final String JWT_CLAIM_USERNAME          = "username";
    public static final String JWT_CLAIM_EMAIL             = "email";
    // ==================== DEFAULT VALUES ====================
    public static final int    DEFAULT_BCRYPT_STRENGTH     = 10;
    public static final int    DEFAULT_JWT_EXPIRATION      = 1800;
}
