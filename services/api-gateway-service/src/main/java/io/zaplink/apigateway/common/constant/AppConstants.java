package io.zaplink.apigateway.common.constant;

public class AppConstants
{
    // Prevent instantiation
    private AppConstants()
    {
        throw new UnsupportedOperationException( "Class cannot be instantiated" );
    }
    // ==================== HEADERS ====================
    public static final String TRACE_ID_HEADER        = "traceId";
    public static final String TRACE_ID_MDC_KEY       = "traceId";
    // ==================== TRACE ID VALUES ====================
    public static final String TRACE_ID_NOT_AVAILABLE = "N/A";
    public static final String TRACE_ID_DEFAULT_VALUE = "-";
}
