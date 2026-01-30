package io.zaplink.social.common.constants;

/**
 * API Endpoint definitions and HTTP Header constants.
 * <p>
 * Defines the contract for URL paths and header keys used by the Social Controller.
 * </p>
 */
public final class ApiConstants
{
    private ApiConstants()
    {
    }
    // Base Paths
    public static final String BASE_URI       = "/social";
    // Endpoints
    public static final String CONNECT_URI    = "/connect";
    public static final String PUBLISH_URI    = "/publish";
    // Headers
    public static final String HEADER_USER_ID = "X-User-Id";
}
