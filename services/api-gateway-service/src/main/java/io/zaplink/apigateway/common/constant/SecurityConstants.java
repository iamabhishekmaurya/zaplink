package io.zaplink.apigateway.common.constant;

/**
 * Security-related constants for API Gateway service.
 * Contains only security constants used by the API Gateway.
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
    
    // ==================== AUTHENTICATION HEADERS ====================
    public static final String AUTH_HEADER           = "Authorization";
    public static final String BEARER_PREFIX          = "Bearer ";
    public static final int    BEARER_PREFIX_LENGTH   = 7;
}
