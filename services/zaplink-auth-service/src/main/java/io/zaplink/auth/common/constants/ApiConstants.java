package io.zaplink.auth.common.constants;

/**
 * API-related constants for endpoints, error codes, and response messages.
 * Contains all REST API and HTTP-related constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-25
 */
public final class ApiConstants
{
    // Prevent instantiation
    private ApiConstants()
    {
        throw new UnsupportedOperationException( "Class cannot be instantiated" );
    }
    // ==================== ENDPOINT PATHS ====================
    public static final String AUTH_BASE_PATH                         = "/auth/**";
    public static final String AUTH_V1_BASE_PATH                      = "/v1/api/auth/**";
    public static final String ERROR_PATH                             = "/error";
    public static final String ACTUATER_PATH                          = "/actuator/**";
    // ==================== ERROR CODES ====================
    public static final String ERROR_USER_NOT_FOUND                   = "USER_NOT_FOUND";
    public static final String ERROR_USER_ALREADY_EXISTS              = "USER_ALREADY_EXISTS";
    public static final String ERROR_INVALID_CREDENTIALS              = "INVALID_CREDENTIALS";
    public static final String ERROR_AUTHENTICATION_FAILED            = "AUTHENTICATION_FAILED";
    public static final String ERROR_ACCESS_DENIED                    = "ACCESS_DENIED";
    public static final String ERROR_VALIDATION_ERROR                 = "VALIDATION_ERROR";
    public static final String ERROR_INTERNAL_SERVER_ERROR            = "INTERNAL_SERVER_ERROR";
    public static final String ERROR_AUTH_ERROR                       = "AUTH_ERROR";
    // ==================== MESSAGES ====================
    public static final String MESSAGE_USER_NOT_FOUND                 = "User not found";
    public static final String MESSAGE_USER_NOT_FOUND_WITH_ID         = "User not found with ID: ";
    public static final String MESSAGE_USER_NOT_FOUND_WITH_EMAIL      = "User not found with email: ";
    public static final String MESSAGE_USER_ACCOUNT_DEACTIVATED       = "User account is deactivated";
    public static final String MESSAGE_USER_ACCOUNT_DEACTIVATED_EMAIL = "User account is deactivated: ";
    public static final String MESSAGE_USER_EMAIL_ALREADY_EXISTS      = "User with email ";
    public static final String MESSAGE_USER_USERNAME_ALREADY_EXISTS   = "User with username ";
    public static final String MESSAGE_ALREADY_EXISTS_SUFFIX          = " already exists";
    public static final String MESSAGE_TOKEN_TRUNCATED_SUFFIX         = "...";
    public static final String MESSAGE_INVALID_EMAIL_OR_PASSWORD      = "Invalid email or password";
    public static final String MESSAGE_INVALID_REFRESH_TOKEN          = "Invalid refresh token";
    public static final String MESSAGE_REFRESH_TOKEN_EXPIRED          = "Refresh token expired";
    public static final String MESSAGE_INVALID_VERIFICATION_TOKEN     = "Invalid verification token";
    public static final String MESSAGE_INVALID_RESET_TOKEN            = "Invalid reset token";
    public static final String MESSAGE_RESET_TOKEN_EXPIRED            = "Reset token expired";
    public static final String MESSAGE_LOGIN_SUCCESSFUL               = "Login successful";
    public static final String MESSAGE_USER_REGISTERED_SUCCESSFULLY   = "User registered successfully";
    public static final String MESSAGE_VERIFICATION_EMAIL_SENT        = "Verification email sent";
    public static final String MESSAGE_EMAIL_VERIFIED_SUCCESSFULLY    = "Email verified successfully";
    public static final String MESSAGE_PASSWORD_RESET_EMAIL_SENT      = "Password reset email sent";
    public static final String MESSAGE_PASSWORD_RESET_SUCCESSFULLY    = "Password reset successfully";
    public static final String MESSAGE_LOGOUT_SUCCESSFUL              = "Logout successful";
    public static final String MESSAGE_USER_UPDATED_SUCCESSFULLY      = "User updated successfully";
    public static final String MESSAGE_USER_DEACTIVATED_SUCCESSFULLY  = "User deactivated successfully";
    public static final String MESSAGE_USER_ACTIVATED_SUCCESSFULLY    = "User activated successfully";
    public static final String MESSAGE_AUTHENTICATION_FAILED          = "Authentication failed";
    public static final String MESSAGE_ACCESS_DENIED                  = "Access denied";
    public static final String MESSAGE_VALIDATION_FAILED              = "Validation failed";
    public static final String MESSAGE_UNEXPECTED_ERROR               = "An unexpected error occurred";
    // ==================== PROBLEM DETAILS ====================
    public static final String PROBLEM_TYPE_DEFAULT                   = "about:blank";
    public static final String PROBLEM_TITLE_AUTH_ERROR               = "Authentication Error";
    public static final String PROBLEM_TITLE_USER_ALREADY_EXISTS      = "User Already Exists";
    public static final String PROBLEM_TITLE_USER_NOT_FOUND           = "User Not Found";
    public static final String PROBLEM_TITLE_INVALID_CREDENTIALS      = "Invalid Credentials";
    public static final String PROBLEM_TITLE_AUTH_FAILED              = "Authentication Failed";
    public static final String PROBLEM_TITLE_BAD_CREDENTIALS          = "Bad Credentials";
    public static final String PROBLEM_TITLE_ACCESS_DENIED            = "Access Denied";
    public static final String PROBLEM_TITLE_VALIDATION_FAILED        = "Validation Failed";
    public static final String PROBLEM_TITLE_INTERNAL_ERROR           = "Internal Server Error";
    public static final String PROBLEM_PROPERTY_ERROR_CODE            = "errorCode";
    public static final String PROBLEM_PROPERTY_TRACE_ID              = "traceId";
    public static final String PROBLEM_PROPERTY_ERRORS                = "errors";
    // ==================== OPERATIONS ====================
    public static final String OPERATION_UPDATE                       = "update";
    public static final String OPERATION_DEACTIVATION                 = "deactivation";
    public static final String OPERATION_ACTIVATION                   = "activation";
    public static final String OPERATION_LOGIN                        = "login";
    public static final String OPERATION_PASSWORD_RESET               = "password reset";
    public static final String OPERATION_GET_CURRENT_USER             = "get current user";
    public static final String OPERATION_AUTHENTICATION               = "authentication";
    // ==================== EMAIL SUBJECTS ====================
    public static final String SUBJECT_VERIFICATION_EMAIL             = "Zaplink Verification Email";
    // ==================== SWAGGER TAGS ====================
    public static final String TAG_AUTHENTICATION                     = "Authentication";
    public static final String TAG_AUTHENTICATION_DESC                = "APIs for user registration, login, token management, and password operations";
    public static final String TAG_USER_MANAGEMENT                    = "User Management";
    public static final String TAG_USER_MANAGEMENT_DESC               = "APIs for user profile management and administration";
    // ==================== AUTH CONTROLLER OPERATIONS ====================
    public static final String AUTH_REGISTER_SUMMARY                  = "Register a new user";
    public static final String AUTH_REGISTER_DESC                     = "Creates a new user account with the provided registration details";
    public static final String AUTH_LOGIN_SUMMARY                     = "User login";
    public static final String AUTH_LOGIN_DESC                        = "Authenticates a user and returns JWT access and refresh tokens";
    public static final String AUTH_GET_CURRENT_USER_SUMMARY          = "Get current user";
    public static final String AUTH_GET_CURRENT_USER_DESC             = "Retrieves the currently authenticated user's information";
    public static final String AUTH_REFRESH_TOKEN_SUMMARY             = "Refresh access token";
    public static final String AUTH_REFRESH_TOKEN_DESC                = "Generates new JWT tokens using a valid refresh token";
    public static final String AUTH_LOGOUT_SUMMARY                    = "User logout";
    public static final String AUTH_LOGOUT_DESC                       = "Logs out the user by invalidating their refresh token";
    public static final String AUTH_VERIFY_EMAIL_SUMMARY              = "Verify email";
    public static final String AUTH_VERIFY_EMAIL_DESC                 = "Verifies a user's email address using a verification token";
    public static final String AUTH_RESEND_VERIFICATION_SUMMARY       = "Resend verification email";
    public static final String AUTH_RESEND_VERIFICATION_DESC          = "Resends the verification email to a user";
    public static final String AUTH_REQUEST_PASSWORD_RESET_SUMMARY    = "Request password reset";
    public static final String AUTH_REQUEST_PASSWORD_RESET_DESC       = "Initiates password reset process by sending a reset email";
    public static final String AUTH_RESET_PASSWORD_SUMMARY            = "Reset password";
    public static final String AUTH_RESET_PASSWORD_DESC               = "Resets user password using a valid reset token";
    // ==================== USER CONTROLLER OPERATIONS ====================
    public static final String USER_GET_SUMMARY                       = "Get user by ID";
    public static final String USER_GET_DESC                          = "Retrieves a user's profile by their ID";
    public static final String USER_UPDATE_SUMMARY                    = "Update user";
    public static final String USER_UPDATE_DESC                       = "Updates a user's profile information";
    public static final String USER_DEACTIVATE_SUMMARY                = "Deactivate user";
    public static final String USER_DEACTIVATE_DESC                   = "Deactivates a user account (Admin only)";
    public static final String USER_ACTIVATE_SUMMARY                  = "Activate user";
    public static final String USER_ACTIVATE_DESC                     = "Activates a previously deactivated user account (Admin only)";
    // ==================== SWAGGER RESPONSES ====================
    public static final String RESPONSE_201_USER_REGISTERED           = "User registered successfully";
    public static final String RESPONSE_200_LOGIN_SUCCESSFUL          = "Login successful, tokens returned";
    public static final String RESPONSE_200_USER_INFO_RETRIEVED       = "User information retrieved successfully";
    public static final String RESPONSE_200_TOKEN_REFRESHED           = "Tokens refreshed successfully";
    public static final String RESPONSE_200_LOGOUT_SUCCESSFUL         = "Logout successful";
    public static final String RESPONSE_200_EMAIL_VERIFIED            = "Email verified successfully";
    public static final String RESPONSE_200_VERIFICATION_EMAIL_SENT   = "Verification email sent";
    public static final String RESPONSE_200_PASSWORD_RESET_EMAIL_SENT = "Password reset email sent";
    public static final String RESPONSE_200_PASSWORD_RESET_SUCCESSFUL = "Password reset successfully";
    public static final String RESPONSE_200_USER_RETRIEVED            = "User retrieved successfully";
    public static final String RESPONSE_200_USER_UPDATED              = "User updated successfully";
    public static final String RESPONSE_200_USER_DEACTIVATED          = "User deactivated successfully";
    public static final String RESPONSE_200_USER_ACTIVATED            = "User activated successfully";
    public static final String RESPONSE_400_INVALID_INPUT             = "Invalid input data";
    public static final String RESPONSE_400_INVALID_TOKEN             = "Invalid token provided";
    public static final String RESPONSE_401_UNAUTHORIZED              = "Unauthorized - Invalid credentials";
    public static final String RESPONSE_401_INVALID_REFRESH_TOKEN     = "Invalid or expired refresh token";
    public static final String RESPONSE_403_ACCESS_DENIED             = "Access denied";
    public static final String RESPONSE_404_USER_NOT_FOUND            = "User not found";
    public static final String RESPONSE_409_USER_ALREADY_EXISTS       = "User with this email already exists";
    // ==================== SWAGGER PARAMETERS ====================
    public static final String PARAM_USER_ID                          = "User ID";
    public static final String PARAM_REFRESH_TOKEN                    = "Refresh token";
    public static final String PARAM_VERIFICATION_TOKEN               = "Email verification token";
    public static final String PARAM_EMAIL                            = "User email address";
    public static final String PARAM_RESET_TOKEN                      = "Password reset token";
    public static final String PARAM_NEW_PASSWORD                     = "New password";
}
