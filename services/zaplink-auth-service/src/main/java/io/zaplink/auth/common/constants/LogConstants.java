package io.zaplink.auth.common.constants;

/**
 * Logging-related constants for log message templates and logging patterns.
 * Contains all structured logging message templates used throughout the application.
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
    // ==================== USER SERVICE LOGS ====================
    public static final String LOG_SEARCHING_USER_BY_EMAIL                  = "Searching for user by email: {}";
    public static final String LOG_SEARCHING_USER_BY_USERNAME               = "Searching for user by username: {}";
    public static final String LOG_ASSIGNING_DEFAULT_USER_ROLE              = "Assigning default USER role to new user";
    public static final String LOG_DEFAULT_USER_ROLE_NOT_FOUND              = "Default USER role not found, creating new role";
    public static final String LOG_USER_CREATED_SUCCESSFULLY                = "User created successfully - ID: {}, Email: {}, Username: {}";
    public static final String LOG_SAVING_USER_ENTITY                       = "Saving user entity for user ID: {}";
    public static final String LOG_GET_USER_REQUEST                         = "Get user request received for ID: {}";
    public static final String LOG_UPDATE_USER_REQUEST                      = "Update user request received for ID: {}";
    public static final String LOG_UPDATING_USER_INFORMATION                = "Updating user information for user ID: {}";
    public static final String LOG_USER_UPDATED_SUCCESSFULLY                = "User updated successfully - ID: {}, Email: {}";
    public static final String LOG_DEACTIVATE_USER_REQUEST                  = "Deactivate user request received for ID: {}";
    public static final String LOG_DEACTIVATING_USER_ACCOUNT                = "Deactivating user account for user ID: {}";
    public static final String LOG_USER_DEACTIVATED_SUCCESSFULLY            = "User deactivated successfully - ID: {}, Email: {}";
    public static final String LOG_ACTIVATE_USER_REQUEST                    = "Activate user request received for ID: {}";
    public static final String LOG_ACTIVATING_USER_ACCOUNT                  = "Activating user account for user ID: {}";
    public static final String LOG_USER_ACTIVATED_SUCCESSFULLY              = "User activated successfully - ID: {}, Email: {}";
    public static final String LOG_USER_NOT_FOUND_FOR                       = "User not found for {}: {}";
    public static final String LOG_USER_NOT_FOUND_FOR_EMAIL                 = "User not found for email: {}";
    public static final String LOG_ATTEMPTED_FOR_DEACTIVATED_USER           = "Attempted {} for deactivated user: {}";
    // ==================== REGISTRATION LOGS ====================
    public static final String LOG_STARTING_USER_REGISTRATION               = "Starting user registration process for email: {}";
    public static final String LOG_CHECKING_EMAIL_EXISTS                    = "Checking if email already exists: {}";
    public static final String LOG_CHECKING_USERNAME_EXISTS                 = "Checking if username already exists: {}";
    public static final String LOG_REGISTRATION_ATTEMPT_EXISTING_EMAIL      = "Registration attempt with existing email: {}";
    public static final String LOG_REGISTRATION_ATTEMPT_EXISTING_USERNAME   = "Registration attempt with existing username: {}";
    public static final String LOG_CREATING_NEW_USER_ACCOUNT                = "Creating new user account";
    public static final String LOG_USER_REGISTERED_SUCCESSFULLY             = "User registered successfully - ID: {}, Email: {}, Username: {}";
    public static final String LOG_VERIFICATION_EMAIL_SHOULD_BE_SENT        = "Verification email should be sent to: {}";
    public static final String LOG_USER_REGISTRATION_RESPONSE_CREATED       = "User registration response created for user ID: {}";
    public static final String LOG_PROCESSING_EMAIL_VERIFICATION            = "Processing email verification with token: {}";
    public static final String LOG_EMAIL_VERIFICATION_INVALID_TOKEN         = "Email verification attempted with invalid token: {}";
    public static final String LOG_EMAIL_ALREADY_VERIFIED                   = "Email already verified for user: {} (ID: {})";
    public static final String LOG_EMAIL_VERIFIED_SUCCESSFULLY              = "Email verified successfully for user: {} (ID: {})";
    public static final String LOG_PROCESSING_RESEND_VERIFICATION_EMAIL     = "Processing resend verification email request for: {}";
    public static final String LOG_RESEND_VERIFICATION_NON_EXISTENT_EMAIL   = "Resend verification requested for non-existent email: {}";
    public static final String LOG_USER_ALREADY_VERIFIED                    = "User {} is already verified, no email sent";
    public static final String LOG_NEW_VERIFICATION_TOKEN_GENERATED         = "New verification token generated for user: {} (ID: {})";
    public static final String LOG_PROCESSING_RESEND_VERIFICATION           = "Processing resend verification request for: {}";
    // ==================== AUTHENTICATION LOGS ====================
    public static final String LOG_ATTEMPTING_LOGIN                         = "Attempting login for email: {}";
    public static final String LOG_AUTHENTICATING_USER_SPRING_SECURITY      = "Authenticating user with Spring Security";
    public static final String LOG_USER_AUTHENTICATION_SUCCESSFUL           = "User authentication successful for email: {}";
    public static final String LOG_USER_NOT_FOUND_AFTER_AUTH                = "User not found in database after successful authentication: {}";
    public static final String LOG_LOGIN_ATTEMPT_DEACTIVATED_USER           = "Login attempt for deactivated user account: {}";
    public static final String LOG_GENERATING_JWT_TOKENS                    = "Generating JWT tokens for user: {}";
    public static final String LOG_GENERATING_JWT_TOKEN                     = "Generating JWT token for user: {}";
    public static final String LOG_GENERATING_JWT_TOKEN_WITH_CLAIMS         = "Generating JWT token for user: {} with {} extra claims";
    public static final String LOG_CREATING_JWT_TOKEN                       = "Creating JWT token with subject: {} and expiration: {} seconds";
    public static final String LOG_JWT_TOKEN_CREATED_SUCCESSFULLY           = "JWT token created successfully, expires at: {}";
    public static final String LOG_VALIDATING_JWT_TOKEN                     = "Validating JWT token for user: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_SUCCESSFUL          = "JWT token validation successful for user: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_FAILED              = "JWT token validation failed for user: {} - username match: {}, expired: {}";
    public static final String LOG_JWT_TOKEN_VALIDATION_EXCEPTION           = "JWT token validation failed with exception: {}";
    public static final String LOG_EXTRACTING_USERNAME_FROM_TOKEN           = "Extracting username from JWT token";
    public static final String LOG_EXTRACTING_EXPIRATION_FROM_TOKEN         = "Extracting expiration date from JWT token";
    public static final String LOG_EXTRACTING_CLAIM_FROM_TOKEN              = "Extracting claim from JWT token";
    public static final String LOG_PARSING_JWT_TOKEN_SIGNATURE              = "Parsing and validating JWT token signature";
    public static final String LOG_CHECKING_JWT_TOKEN_EXPIRATION            = "Checking JWT token expiration";
    public static final String LOG_GENERATING_HMAC_SIGNING_KEY              = "Generating HMAC signing key from secret";
    public static final String LOG_JWT_EXPIRATION_TIME                      = "JWT expiration time: {} seconds";
    public static final String LOG_LOGIN_SUCCESSFUL                         = "Login successful for user: {} (ID: {})";
    public static final String LOG_LOGIN_RESPONSE_CREATED                   = "Login response created with access token expiry: {} seconds";
    public static final String LOG_LOGIN_FAILED                             = "Login failed for email: {} - Error: {}";
    public static final String LOG_PROCESSING_REFRESH_TOKEN_REQUEST         = "Processing refresh token request";
    public static final String LOG_REFRESH_TOKEN_NOT_FOUND                  = "Refresh token not found in database: {}";
    public static final String LOG_REFRESH_TOKEN_EXPIRED                    = "Refresh token expired for user: {} (Token ID: {})";
    public static final String LOG_REFRESH_TOKEN_VALIDATED                  = "Refresh token validated for user: {}";
    public static final String LOG_OLD_REFRESH_TOKEN_REMOVED                = "Old refresh token removed for user: {}";
    public static final String LOG_TOKEN_REFRESH_SUCCESSFUL                 = "Token refresh successful for user: {}";
    public static final String LOG_GENERATING_REFRESH_TOKEN                 = "Generating refresh token for user: {}";
    public static final String LOG_REFRESH_TOKEN_CREATED                    = "Refresh token created for user: {} (expires: {})";
    public static final String LOG_PROCESSING_LOGOUT_REQUEST                = "Processing logout request";
    public static final String LOG_LOGOUT_INVALID_REFRESH_TOKEN             = "Logout attempt with invalid refresh token: {}";
    public static final String LOG_USER_LOGGED_OUT_SUCCESSFULLY             = "User logged out successfully: {}";
    public static final String LOG_PROCESSING_GET_CURRENT_USER_REQUEST      = "Processing get current user request";
    public static final String LOG_GET_CURRENT_USER_SUCCESSFUL              = "Successfully retrieved current user information: {}";
    // ==================== PASSWORD RESET LOGS ====================
    public static final String LOG_PROCESSING_PASSWORD_RESET_REQUEST        = "Processing password reset request for email: {}";
    public static final String LOG_PASSWORD_RESET_NON_EXISTENT_EMAIL        = "Password reset requested for non-existent email: {}";
    public static final String LOG_PASSWORD_RESET_TOKEN_GENERATED           = "Password reset token generated for user: {} (expires: {})";
    public static final String LOG_PASSWORD_RESET_EMAIL_SHOULD_BE_SENT      = "Password reset email should be sent to: {}";
    public static final String LOG_PROCESSING_PASSWORD_RESET_WITH_TOKEN     = "Processing password reset with token";
    public static final String LOG_PASSWORD_RESET_INVALID_TOKEN             = "Password reset attempted with invalid token: {}";
    public static final String LOG_PASSWORD_RESET_TOKEN_EXPIRED             = "Password reset token expired for user: {} (expired: {})";
    public static final String LOG_PASSWORD_RESET_SUCCESSFUL                = "Password reset successful for user: {}";
    public static final String LOG_ALL_REFRESH_TOKENS_INVALIDATED           = "All refresh tokens invalidated for user: {}";
    // ==================== SECURITY CONFIGURATION LOGS ====================
    public static final String LOG_LOADING_USER_BY_EMAIL                    = "Loading user by email: {}";
    public static final String LOG_CONFIGURING_BCRYPT_PASSWORD_ENCODER      = "Configuring BCrypt password encoder";
    public static final String LOG_CONFIGURING_SPRING_SECURITY_AUTH_MANAGER = "Configuring Spring Security authentication manager";
    public static final String LOG_CONFIGURING_SECURITY_FILTER_CHAIN        = "Configuring security filter chain";
    public static final String LOG_DISABLING_CSRF_PROTECTION                = "Disabling CSRF protection for JWT-based authentication";
    public static final String LOG_CONFIGURING_STATELESS_SESSION_MANAGEMENT = "Configuring stateless session management";
    public static final String LOG_CONFIGURING_ENDPOINT_AUTHORIZATION_RULES = "Configuring endpoint authorization rules";
    public static final String LOG_SPRING_SECURITY_FILTER_CHAIN_CONFIGURED  = "Spring Security filter chain configured successfully";
    public static final String LOG_PUBLIC_ENDPOINTS                         = "Public endpoints: /auth/**, /v1/auth/**, /error";
    public static final String LOG_ALL_OTHER_ENDPOINTS_REQUIRE_AUTH         = "All other endpoints require authentication";
    // ==================== TRACE ID LOGS ====================
    public static final String LOG_TRACE_ID_SET_FOR_THREAD                  = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD              = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER                 = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW                   = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE               = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE             = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== EXCEPTION HANDLING LOGS ====================
    public static final String LOG_AUTH_EXCEPTION_OCCURRED                  = "AuthException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_ALREADY_EXISTS_EXCEPTION            = "UserAlreadyExistsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_NOT_FOUND_EXCEPTION                 = "UserNotFoundException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_INVALID_CREDENTIALS_EXCEPTION            = "InvalidCredentialsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_AUTHENTICATION_EXCEPTION                 = "AuthenticationException occurred - TraceId: {}, Error: {}";
    public static final String LOG_BAD_CREDENTIALS_EXCEPTION                = "BadCredentialsException occurred - TraceId: {}, Error: {}";
    public static final String LOG_ACCESS_DENIED_EXCEPTION                  = "AccessDeniedException occurred - TraceId: {}, Error: {}";
    public static final String LOG_VALIDATION_EXCEPTION_OCCURRED            = "MethodArgumentNotValidException occurred - TraceId: {}, Validation errors: {}";
    public static final String LOG_UNEXPECTED_EXCEPTION_OCCURRED            = "Unexpected exception occurred - TraceId: {}, Error: {}";
}