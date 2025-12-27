package io.zaplink.manager.common.constants;

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
    public static final String LOG_TRACE_ID_SET_FOR_THREAD          = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD      = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER         = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW           = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE       = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE     = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== REDIS SERVICE LOGS ====================
    public static final String LOG_REDIS_SET_KEY                    = "Going to set key: {} and value: {}";
    // ==================== URL MANAGER SERVICE LOGS ====================
    public static final String LOG_URL_FOUND_IN_REDIS               = "Url found in redis for key: {}";
    public static final String LOG_URL_NOT_FOUND_IN_REDIS           = "Url not found in redis for key: {}";
    public static final String LOG_URL_FOUND_IN_DB                  = "Url found in database for key: {}";
    public static final String LOG_URL_NOT_FOUND_IN_DB              = "Url not found in database for key: {}";
    public static final String LOG_EXCEPTION_FETCHING_URL           = "Exception while fetching url for key: {}. Error: {}";
    public static final String LOG_CLICK_EVENT_PUBLISHED            = "Going to publish click event for key: {}";
    public static final String LOG_EXCEPTION_PUBLISHING_CLICK_EVENT = "Exception while publishing click event for key: {}. Error: {}";
}