package io.zaplink.auth.common.constants;

/**
 * Security-related constants for headers, roles, and other security configurations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-25
 */
public final class SecurityConstants
{
    // Prevent instantiation
    private SecurityConstants()
    {
        throw new UnsupportedOperationException( "Class cannot be instantiated" );
    }
    // ==================== HEADERS ====================
    public static final String HEADER_X_USER_EMAIL = "X-User-Email";
    // ==================== ROLES ====================
    public static final String ROLE_USER           = "USER";
    public static final String ROLE_ADMIN          = "ADMIN";
    public static final String ROLE_PREFIX         = "ROLE_";
    public static final String OP_SEARCH_USER_ID   = "Searching user by id: {}";
    // ==================== ATTRIBUTES ====================
    public static final String ATTRIBUTE_USER_ID   = "userId";
    // ==================== TOKENS ====================
    public static final String TOKEN_TYPE_BEARER   = "Bearer ";
    public static final String CLAIM_USER_ID       = "userId";
    public static final String CLAIM_USERNAME      = "username";
}
