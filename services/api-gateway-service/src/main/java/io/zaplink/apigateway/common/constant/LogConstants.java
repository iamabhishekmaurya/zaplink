package io.zaplink.apigateway.common.constant;

/**
 * Logging-related constants for API Gateway service.
 * Contains only log message templates used by the API Gateway.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
public final class LogConstants
{
    // Prevent instantiation
    private LogConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    
    // ==================== JWT LOGS ====================
    public static final String LOG_GENERATING_HMAC_SIGNING_KEY              = "Generating HMAC signing key from secret";
    public static final String LOG_EXTRACTING_USERNAME_FROM_TOKEN           = "Extracting username from JWT token";
    public static final String LOG_EXTRACTING_EXPIRATION_FROM_TOKEN         = "Extracting expiration date from JWT token";
    public static final String LOG_EXTRACTING_CLAIM_FROM_TOKEN              = "Extracting claim from JWT token";
    public static final String LOG_PARSING_JWT_TOKEN_SIGNATURE              = "Parsing and validating JWT token signature";
    public static final String LOG_CHECKING_JWT_TOKEN_EXPIRATION            = "Checking JWT token expiration";
    public static final String LOG_JWT_EXPIRATION_TIME                      = "JWT expiration time: {} seconds";
    public static final String LOG_GENERATING_JWT_TOKEN                     = "Generating JWT token for user: {}";
    public static final String LOG_GENERATING_JWT_TOKEN_WITH_CLAIMS         = "Generating JWT token for user: {} with {} extra claims";
    public static final String LOG_CREATING_JWT_TOKEN                       = "Creating JWT token with subject: {} and expiration: {} seconds";
    public static final String LOG_JWT_TOKEN_CREATED_SUCCESSFULLY           = "JWT token created successfully, expires at: {}";
    public static final String LOG_VALIDATING_JWT_TOKEN                     = "Validating JWT token for user: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_SUCCESSFUL          = "JWT token validation successful for user: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_FAILED              = "JWT token validation failed for user: {} - username match: {}, expired: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_EXCEPTION           = "JWT token validation failed with exception: {}";
    
    // ==================== GATEWAY FILTER LOGS ====================
    public static final String LOG_GATEWAY_FILTER_PROCESSING_REQUEST        = "Gateway filter processing request - Method: {}, URI: {}";
    public static final String LOG_GATEWAY_FILTER_AUTH_HEADER_MISSING       = "Gateway filter - Authorization header missing";
    public static final String LOG_GATEWAY_FILTER_TOKEN_VALIDATION_PASSED   = "Gateway filter - JWT token validation passed for user: {}";
    public static final String LOG_GATEWAY_FILTER_TOKEN_VALIDATION_FAILED   = "Gateway filter - JWT token validation failed: {}";
    public static final String LOG_GATEWAY_FILTER_SETTING_AUTHENTICATION    = "Gateway filter - Setting authentication for user: {}";
    
    // ==================== SECURITY CONFIGURATION LOGS ====================
    public static final String LOG_CONFIGURING_SECURITY_FILTER_CHAIN        = "Configuring security filter chain";
}
