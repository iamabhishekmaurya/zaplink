package io.zaplink.social.common.constants;

/**
 * HTTP status code constants for Swagger documentation.
 */
public final class StatusConstants
{
    private StatusConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    public static final String STATUS_200_OK                    = "200";
    public static final String STATUS_400_BAD_REQUEST           = "400";
    public static final String STATUS_401_UNAUTHORIZED          = "401";
    public static final String STATUS_403_FORBIDDEN             = "403";
    public static final String STATUS_404_NOT_FOUND             = "404";
    public static final String STATUS_500_INTERNAL_SERVER_ERROR = "500";
}
