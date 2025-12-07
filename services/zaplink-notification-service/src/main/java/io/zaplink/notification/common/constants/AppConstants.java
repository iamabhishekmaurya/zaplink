package io.zaplink.notification.common.constants;

/**
 * Application-related constants for the notification service.
 * Contains template paths, error messages, and other application constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-07
 */
public final class AppConstants
{
    // Prevent instantiation
    private AppConstants()
    {
        throw new UnsupportedOperationException( "Constants class cannot be instantiated" );
    }
    // ==================== EMAIL TEMPLATES ====================
    public static final String TEMPLATE_REGISTRATION_VERIFICATION      = "templates/registration-verification.html";
    // ==================== TEMPLATE PLACEHOLDERS ====================
    public static final String VERIFICATION_CODE_PLACEHOLDER           = "{{VERIFICATION_CODE}}";
    public static final String YEAR_PLACEHOLDER                        = "{{YEAR}}";
    // ==================== HEADERS ====================
    public static final String TRACE_ID_HEADER                         = "traceId";
    public static final String TRACE_ID_MDC_KEY                        = "traceId";
    // ==================== SUCCESS MESSAGES ====================
    public static final String EMAIL_SENT_SUCCESS_MESSAGE              = "Email sent successfully";
    // ==================== ERROR MESSAGES ====================
    public static final String ERROR_EMAIL_SENDING_FAILED              = "Email sending failed";
    public static final String ERROR_EMAIL_PROCESSING_FAILED           = "Email processing failed";
    public static final String ERROR_FAILED_TO_SEND_VERIFICATION_EMAIL = "Failed to send verification email: ";
    public static final String ERROR_FAILED_TO_PREPARE_EMAIL           = "Failed to prepare email";
    public static final String ERROR_FAILED_TO_SEND_EMAIL              = "Failed to send email: ";
    public static final String ERROR_TEMPLATE_NOT_FOUND                = "Template not found: ";
    // ==================== HTTP RESPONSE VALUES ====================
    public static final String HTTP_INTERNAL_SERVER_ERROR              = "Internal Server Error";
    public static final String HTTP_RUNTIME_ERROR                      = "Runtime Error";
    public static final String HTTP_UNEXPECTED_ERROR_MESSAGE           = "An unexpected error occurred";
    // ==================== JSON RESPONSE KEYS ====================
    public static final String JSON_KEY_STATUS                         = "status";
    public static final String JSON_KEY_ERROR                          = "error";
    public static final String JSON_KEY_MESSAGE                        = "message";
    public static final String JSON_KEY_TRACE_ID                       = "traceId";
    // ==================== TRACE ID VALUES ====================
    public static final String TRACE_ID_NOT_AVAILABLE                  = "N/A";
    public static final String TRACE_ID_DEFAULT_VALUE                  = "-";
}
