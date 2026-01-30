package io.zaplink.social.common.constants;

/**
 * Validation and Exception error messages.
 * <p>
 * Centralizes strings used in exceptions and error responses to ensure consistency
 * and simplify future localization efforts.
 * </p>
 */
public final class ErrorMessages
{
    private ErrorMessages()
    {
    }
    public static final String INVALID_PROVIDER  = "Unsupported or invalid social provider: ";
    public static final String ACCOUNT_NOT_FOUND = "Social account not found with ID: ";
    public static final String DB_SAVE_FAILED    = "Failed to save social account connection";
    public static final String PUBLISH_FAILED    = "External publishing failed";
}
