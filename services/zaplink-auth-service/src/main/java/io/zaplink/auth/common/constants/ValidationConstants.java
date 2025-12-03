package io.zaplink.auth.common.constants;

/**
 * Validation-related constants for field constraints, validation messages, and size limits.
 * Contains all validation rules and error messages for DTOs and entities.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
public final class ValidationConstants
{
    // Prevent instantiation
    private ValidationConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== VALIDATION CONSTRAINTS ====================
    public static final int    USERNAME_MIN_LENGTH          = 3;
    public static final int    USERNAME_MAX_LENGTH          = 50;
    public static final int    PASSWORD_MIN_LENGTH          = 6;
    public static final int    PASSWORD_MAX_LENGTH          = 100;
    public static final int    FIRST_NAME_MAX_LENGTH        = 50;
    public static final int    LAST_NAME_MAX_LENGTH         = 50;
    public static final int    PHONE_NUMBER_MAX_LENGTH      = 20;
    public static final int    ROLE_NAME_MIN_LENGTH         = 3;
    public static final int    ROLE_NAME_MAX_LENGTH         = 50;
    public static final int    ROLE_DESCRIPTION_MAX_LENGTH  = 200;
    // ==================== VALIDATION MESSAGES ====================
    public static final String VALIDATION_EMAIL_REQUIRED    = "Email is required";
    public static final String VALIDATION_EMAIL_VALID       = "Email should be valid";
    public static final String VALIDATION_PASSWORD_REQUIRED = "Password is required";
    public static final String VALIDATION_USERNAME_REQUIRED = "Username is required";
    public static final String VALIDATION_USERNAME_SIZE     = "Username must be between " + USERNAME_MIN_LENGTH + " and " + USERNAME_MAX_LENGTH + " characters";
    public static final String VALIDATION_PASSWORD_SIZE     = "Password must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters";
    public static final String VALIDATION_FIRST_NAME_SIZE   = "First name must not exceed " + FIRST_NAME_MAX_LENGTH + " characters";
    public static final String VALIDATION_LAST_NAME_SIZE    = "Last name must not exceed " + LAST_NAME_MAX_LENGTH + " characters";
    public static final String VALIDATION_PHONE_NUMBER_SIZE = "Phone number must not exceed " + PHONE_NUMBER_MAX_LENGTH + " characters";
}
