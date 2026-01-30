package io.zaplink.scheduler.common.constants;

/**
 * Validation and Exception Error Messages.
 * <p>
 * Centralizes all error strings used in exceptions and validations.
 * </p>
 */
public final class ErrorMessages
{
    private ErrorMessages()
    {
    }
    public static final String POST_NOT_FOUND              = "Scheduled post not found with ID: ";
    public static final String CANNOT_SCHEDULE_IN_PAST     = "Cannot schedule post in the past";
    public static final String CANNOT_RESCHEDULE_PUBLISHED = "Cannot reschedule a published post";
    public static final String FAILED_TO_SCHEDULE          = "Failed to schedule post";
    public static final String FAILED_TO_RESCHEDULE        = "Failed to reschedule post";
}
