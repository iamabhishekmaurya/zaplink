package io.zaplink.media.common.constants;

/**
 * HTTP status code constants for Swagger documentation.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-02-06
 */
public final class StatusConstants
{
    private StatusConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // Success Status Codes
    public static final String STATUS_200_OK                    = "200";
    public static final String STATUS_201_CREATED               = "201";
    public static final String STATUS_204_NO_CONTENT            = "204";
    // Client Error Status Codes
    public static final String STATUS_400_BAD_REQUEST           = "400";
    public static final String STATUS_401_UNAUTHORIZED          = "401";
    public static final String STATUS_403_FORBIDDEN             = "403";
    public static final String STATUS_404_NOT_FOUND             = "404";
    public static final String STATUS_409_CONFLICT              = "409";
    // Server Error Status Codes
    public static final String STATUS_500_INTERNAL_SERVER_ERROR = "500";
}
