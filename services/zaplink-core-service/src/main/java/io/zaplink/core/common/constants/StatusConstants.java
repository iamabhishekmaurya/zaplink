package io.zaplink.core.common.constants;

/**
 * Centralized constants for HTTP status codes used across the application.
 * This utility class provides a single source of truth for all HTTP status codes,
 * ensuring consistency and maintainability in API responses.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public final class StatusConstants
{
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private StatusConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== SUCCESS STATUS CODES ====================
    /**
     * HTTP 200 OK - Request was successful.
     */
    public static final String   STATUS_200_OK                    = "200";
    /**
     * HTTP 201 Created - Resource was successfully created.
     */
    public static final String   STATUS_201_CREATED               = "201";
    /**
     * HTTP 204 No Content - Request was successful but no content to return.
     */
    public static final String   STATUS_204_NO_CONTENT            = "204";
    // ==================== CLIENT ERROR STATUS CODES ====================
    /**
     * HTTP 400 Bad Request - Invalid request data or parameters.
     */
    public static final String   STATUS_400_BAD_REQUEST           = "400";
    /**
     * HTTP 403 Forbidden - Access is forbidden to the requested resource.
     */
    public static final String   STATUS_403_FORBIDDEN             = "403";
    /**
     * HTTP 404 Not Found - Requested resource could not be found.
     */
    public static final String   STATUS_404_NOT_FOUND             = "404";
    /**
     * HTTP 409 Conflict - Request conflicts with current state of resource.
     */
    public static final String   STATUS_409_CONFLICT              = "409";
    // ==================== SERVER ERROR STATUS CODES ====================
    /**
     * HTTP 500 Internal Server Error - Server encountered unexpected error.
     */
    public static final String   STATUS_500_INTERNAL_SERVER_ERROR = "500";
    // ==================== STATUS CODE GROUPS ====================
    /**
     * Array of all success status codes (2xx).
     */
    public static final String[] SUCCESS_STATUS_CODES             =
    { STATUS_200_OK, STATUS_201_CREATED, STATUS_204_NO_CONTENT };
    /**
     * Array of all client error status codes (4xx).
     */
    public static final String[] CLIENT_ERROR_STATUS_CODES        =
    { STATUS_400_BAD_REQUEST, STATUS_403_FORBIDDEN, STATUS_404_NOT_FOUND, STATUS_409_CONFLICT };
    /**
     * Array of all server error status codes (5xx).
     */
    public static final String[] SERVER_ERROR_STATUS_CODES        =
    { STATUS_500_INTERNAL_SERVER_ERROR };
    /**
     * Array of all error status codes (4xx and 5xx).
     */
    public static final String[] ERROR_STATUS_CODES               =
    { STATUS_400_BAD_REQUEST, STATUS_403_FORBIDDEN, STATUS_404_NOT_FOUND, STATUS_409_CONFLICT,
      STATUS_500_INTERNAL_SERVER_ERROR };
}
