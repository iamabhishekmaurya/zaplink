package io.zaplink.auth.common.util;

import java.time.Instant;

import org.springframework.stereotype.Component;

import io.zaplink.auth.dto.response.BaseResponse;

/**
 * Utility class for common response operations.
 * Provides reusable methods for building standardized responses.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Component
public class ResponseUtility
{
    /**
     * Creates a success response with message.
     * 
     * @param message The success message
     * @return BaseResponse with success status
     */
    public BaseResponse createSuccessResponse( String message )
    {
        return BaseResponse.builder().success( true ).message( message ).timestamp( Instant.now() ).build();
    }

    /**
     * Creates an error response with message.
     * 
     * @param message The error message
     * @return BaseResponse with error status
     */
    public BaseResponse createErrorResponse( String message )
    {
        return BaseResponse.builder().success( false ).message( message ).timestamp( Instant.now() ).build();
    }
}
