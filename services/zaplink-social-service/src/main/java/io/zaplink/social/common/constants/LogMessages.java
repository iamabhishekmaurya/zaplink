package io.zaplink.social.common.constants;

/**
 * Standardized logging templates for the Social Service.
 * <p>
 * Contains format strings for use with SLF4J. Placeholders ({}) are used for dynamic data.
 * Grouped by component/layer for easier navigation.
 * </p>
 */
public final class LogMessages
{
    private LogMessages()
    {
    }
    // --- Service Layer: Connection Flow ---
    public static final String CONNECT_ENTRY                 = "Method: connectAccount - Entry. Provider: '{}', OwnerId: '{}'";
    public static final String AUTH_CODE_DEBUG               = "AuthCode length: {}";
    public static final String CONNECT_VALIDATION_FAILED     = "Method: connectAccount - Validation Failed. Invalid provider: '{}'";
    public static final String CONNECT_INIT_TOKEN_EXCHANGE   = "Method: connectAccount - Initiating OAuth token exchange for provider: {}";
    public static final String CONNECT_EXCHANGE_SUCCESS      = "Method: connectAccount - Token exchange successful. ProviderId: '{}', Expiry: '{}'";
    public static final String ACCOUNT_CONNECTED_INFO        = "Connected social account: {} ({})";
    public static final String CONNECT_EXIT_SUCCESS          = "Method: connectAccount - Exit. Success. AccountID: '{}'";
    public static final String CONNECT_DB_ERROR              = "Method: connectAccount - Database persistence failed for provider: '{}', owner: '{}'";
    // --- Service Layer: Publishing Flow ---
    public static final String PUBLISH_ENTRY                 = "Method: publishPost - Entry. AccountID: '{}'";
    public static final String PUBLISH_DEBUG_INPUT           = "Caption length: {}, MediaUrl provided: {}";
    public static final String PUBLISH_ACCOUNT_LOOKUP_FAILED = "Method: publishPost - Account lookup failed. ID: '{}'";
    public static final String PUBLISH_CALL_EXTERNAL_API     = "Method: publishPost - Calling external API for Provider: '{}'";
    public static final String PUBLISH_LOG_POST_DETAILS      = "Publishing post to {} with caption: {}";
    public static final String PUBLISH_LOG_TARGET_ID         = "Target Provider ID: {}";
    public static final String PUBLISH_LOG_MEDIA             = "Media Asset: {}";
    public static final String PUBLISH_LOG_TOKEN_DEBUG       = "Using AccessToken: {}";
    public static final String PUBLISH_EXIT_SUCCESS          = "Method: publishPost - Exit. Publish successful for AccountID: '{}'";
    public static final String PUBLISH_ERROR                 = "Method: publishPost - Failed to publish to provider '{}'";
    // --- Controller Layer ---
    public static final String CONTROLLER_CONNECT_REQUEST    = "API: connectAccount - Request received. Provider: '{}', UserId: '{}'";
    public static final String CONTROLLER_CONNECT_SUCCESS    = "API: connectAccount - Success. Created AccountID: '{}'";
    public static final String CONTROLLER_PUBLISH_REQUEST    = "API: publishPost - Request received. Target AccountID: '{}'";
    public static final String CONTROLLER_PUBLISH_SUCCESS    = "API: publishPost - Success.";
    // --- Formatting ---
    public static final String LOG_SEPARATOR                 = "--------------------------------------------------";
}
