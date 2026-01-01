package io.zaplink.core.common.constants;

/**
 * Logging-related constants for log message templates and logging patterns.
 * Contains all structured logging message templates used throughout the application.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-26
 */
public final class LogConstants
{
    // Prevent instantiation
    private LogConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== TRACE ID LOGS ====================
    public static final String LOG_TRACE_ID_SET_FOR_THREAD       = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD   = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER      = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW        = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE    = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE  = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== EXCEPTION HANDLING LOGS ====================
    public static final String LOG_AUTH_EXCEPTION_OCCURRED       = "AuthException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_ALREADY_EXISTS_EXCEPTION = "UserAlreadyExistsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_NOT_FOUND_EXCEPTION      = "UserNotFoundException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_INVALID_CREDENTIALS_EXCEPTION = "InvalidCredentialsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_AUTHENTICATION_EXCEPTION      = "AuthenticationException occurred - TraceId: {}, Error: {}";
    public static final String LOG_BAD_CREDENTIALS_EXCEPTION     = "BadCredentialsException occurred - TraceId: {}, Error: {}";
    public static final String LOG_ACCESS_DENIED_EXCEPTION       = "AccessDeniedException occurred - TraceId: {}, Error: {}";
    public static final String LOG_VALIDATION_EXCEPTION_OCCURRED = "MethodArgumentNotValidException occurred - TraceId: {}, Validation errors: {}";
    public static final String LOG_UNEXPECTED_EXCEPTION_OCCURRED = "Unexpected exception occurred - TraceId: {}, Error: {}";
    // ==================== URL SHORTENER LOGS ====================
    public static final String LOG_SHORT_URL_INIT                = "Going to short the url.";
    public static final String LOG_URL_MAPPING_CREATED           = "URL mapping entity created successfully.";
    public static final String LOG_URL_MAPPING_NOT_CREATED       = "URL mapping entity not created.";
    public static final String LOG_URL_SHORTENING_EXCEPTION      = "Exception while shorting the url. Error: {}";
    public static final String LOG_CREATING_URL_MAPPING          = "Creating URL mapping entity. key: {}";
}