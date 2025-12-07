package io.zaplink.notification.common.constants;

/**
 * Logging-related constants for log message templates and logging patterns.
 * Contains all structured logging message templates used throughout the notification service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-07
 */
public final class LogConstants
{
    // Prevent instantiation
    private LogConstants()
    {
        throw new UnsupportedOperationException( "Constants class cannot be instantiated" );
    }
    // ==================== REGISTRATION EMAIL HANDLER LOGS ====================
    public static final String LOG_PROCESSING_REGISTRATION_EMAIL_REQUEST = "Processing registration email request for: {}";
    public static final String LOG_SENDING_EMAIL                         = "Sending registration email to: {}";
    public static final String LOG_EMAIL_SENT_SUCCESSFULLY               = "Registration email sent successfully to: {}";
    public static final String LOG_EMAIL_SENDING_FAILED                  = "Failed to send registration email to: {}";
    public static final String LOG_EMAIL_REQUEST_VALIDATION_FAILED       = "Email request validation failed - Error: {}";
    // ==================== EXCEPTION HANDLER LOGS ====================
    public static final String LOG_UNEXPECTED_EXCEPTION_OCCURRED         = "Unexpected exception occurred - TraceId: {}, Error: {}";
    public static final String LOG_RUNTIME_EXCEPTION_OCCURRED            = "Runtime exception occurred - TraceId: {}, Error: {}";
}
