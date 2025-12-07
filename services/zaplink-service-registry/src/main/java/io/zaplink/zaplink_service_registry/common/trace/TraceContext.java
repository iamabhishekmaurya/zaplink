package io.zaplink.zaplink_service_registry.common.trace;

import java.util.UUID;

/**
 * Thread-local context holder for managing trace IDs throughout the request lifecycle.
 * Provides methods to set, get, and clear trace IDs for the current thread.
 */
public final class TraceContext
{
    private static final String TRACE_ID_HEADER = "X-Trace-ID";
    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();
    public static final String TRACE_ID_KEY = "traceId";

    private TraceContext()
    {
        // Utility class - prevent instantiation
    }

    /**
     * Sets the trace ID for the current thread.
     * 
     * @param traceId The trace ID to set
     */
    public static void setTraceId( String traceId )
    {
        traceIdHolder.set( traceId );
        System.out.println( "Set trace ID " + traceId + " for current thread" );
    }

    /**
     * Gets the trace ID for the current thread.
     * 
     * @return The trace ID, or null if not set
     */
    public static String getTraceId()
    {
        return traceIdHolder.get();
    }

    /**
     * Gets the trace ID for the current thread, or generates a new one if not set.
     * 
     * @return The existing or newly generated trace ID
     */
    public static String getOrGenerateTraceId()
    {
        String traceId = traceIdHolder.get();
        if ( traceId == null )
        {
            traceId = generateTraceId();
            setTraceId( traceId );
        }
        return traceId;
    }

    /**
     * Clears the trace ID for the current thread.
     * Should be called at the end of request processing.
     */
    public static void clearTraceId()
    {
        String traceId = traceIdHolder.get();
        if ( traceId != null )
        {
            System.out.println( "Cleared trace ID " + traceId + " for current thread" );
            traceIdHolder.remove();
        }
    }

    /**
     * Generates a new unique trace ID.
     * 
     * @return A new UUID-based trace ID
     */
    public static String generateTraceId()
    {
        return UUID.randomUUID().toString().replace( "-", "" );
    }

    /**
     * Gets the trace ID header name.
     * 
     * @return The trace ID header name
     */
    public static String getTraceIdHeader()
    {
        return TRACE_ID_HEADER;
    }

    /**
     * Checks if a trace ID is set for the current thread.
     * 
     * @return true if trace ID is set, false otherwise
     */
    public static boolean hasTraceId()
    {
        return traceIdHolder.get() != null;
    }
}
