package io.zaplink.social.common.constants;

/**
 * Controller-related constants for Swagger documentation in Social Service.
 */
public final class ControllerConstants
{
    private ControllerConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String TAG_SOCIAL                     = "Social Media Management";
    public static final String TAG_SOCIAL_DESC                = "APIs for managing social accounts and publishing content";
    // ==================== SOCIAL OPERATIONS ====================
    public static final String SOCIAL_CONNECT_SUMMARY         = "Connect Social Account";
    public static final String SOCIAL_CONNECT_DESC            = "Connects a new social account using an OAuth authorization code";
    public static final String SOCIAL_PUBLISH_SUMMARY         = "Publish Post";
    public static final String SOCIAL_PUBLISH_DESC            = "Publishes a post to a connected social account immediately";
    // ==================== SWAGGER RESPONSE CONSTANTS ====================
    public static final String RESPONSE_200_ACCOUNT_CONNECTED = "Social account connected successfully";
    public static final String RESPONSE_200_POST_PUBLISHED    = "Post published successfully";
    public static final String RESPONSE_400_INVALID_REQUEST   = "Invalid request parameters or payload";
    public static final String RESPONSE_400_PUBLISH_FAILED    = "Failed to publish post due to platform error or invalid data";
    // ==================== SWAGGER PARAMETER CONSTANTS ====================
    public static final String PARAM_PROVIDER                 = "Social platform provider (e.g., INSTAGRAM)";
    public static final String PARAM_AUTH_CODE                = "OAuth2 authorization code from provider";
    public static final String PARAM_USER_ID                  = "User ID (injected by gateway)";
}
