package io.zaplink.auth.common.constants;

/**
 * API-related constants for endpoints, error codes, and response messages.
 * Contains all REST API and HTTP-related constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
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
    public static final String AUTH_V1_BASE_PATH                      = "/v1/auth/**";
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
    // ==================== OPERATIONS ====================
    public static final String OPERATION_UPDATE                       = "update";
    public static final String OPERATION_DEACTIVATION                 = "deactivation";
    public static final String OPERATION_ACTIVATION                   = "activation";
    public static final String OPERATION_LOGIN                        = "login";
    public static final String OPERATION_PASSWORD_RESET               = "password reset";
    public static final String OPERATION_AUTHENTICATION               = "authentication";
}
