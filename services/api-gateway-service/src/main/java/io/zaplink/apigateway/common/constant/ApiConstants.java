package io.zaplink.apigateway.common.constant;

/**
 * API Gateway-specific constants.
 * Contains only constants used by the API Gateway service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-30
 */
public final class ApiConstants
{
    // Prevent instantiation
    private ApiConstants()
    {
        throw new UnsupportedOperationException( "Class cannot be instantiated" );
    }
    // ==================== GATEWAY PATHS ====================
    public static final String AUTH_BASE_PATH    = "/auth/**";
    public static final String AUTH_V1_BASE_PATH = "/v1/auth/**";
    public static final String ACTUATOR_PATH     = "/actuator/**";
    public static final String ERROR_PATH        = "/error";
}
