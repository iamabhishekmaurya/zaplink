package io.zaplink.redirect.common.constants;

/**
 * Controller-related constants for API paths, Swagger documentation, and default values.
 * Contains all controller-specific constants used throughout the Redirect Service REST API layer.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-02-06
 */
public final class ControllerConstants
{
    private ControllerConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String TAG_REDIRECT                    = "Redirect Service";
    public static final String TAG_REDIRECT_DESC               = "High-performance redirect service for short URLs, QR codes, and bio pages";
    // ==================== REDIRECT OPERATIONS ====================
    public static final String REDIRECT_URL_SUMMARY            = "Redirect short URL";
    public static final String REDIRECT_URL_DESC               = "Redirects a short URL to its original destination with analytics tracking";
    public static final String REDIRECT_QR_SUMMARY             = "Redirect QR code";
    public static final String REDIRECT_QR_DESC                = "Redirects a QR code scan to its destination with advanced features like expiration, scan limits, and password protection";
    public static final String REDIRECT_HEALTH_SUMMARY         = "Health check";
    public static final String REDIRECT_HEALTH_DESC            = "Health check endpoint for load balancers";
    public static final String REDIRECT_BIO_PAGE_SUMMARY       = "Get bio page";
    public static final String REDIRECT_BIO_PAGE_DESC          = "Retrieves a bio page by username";
    // ==================== SWAGGER RESPONSE CONSTANTS ====================
    // Success Response Descriptions
    public static final String RESPONSE_302_URL_REDIRECT       = "Redirected to original URL";
    public static final String RESPONSE_302_QR_REDIRECT        = "Redirected to QR destination";
    public static final String RESPONSE_302_PASSWORD_REQUIRED  = "Redirected to password entry page";
    public static final String RESPONSE_302_ERROR_PAGE         = "Redirected to error page";
    public static final String RESPONSE_200_HEALTH_OK          = "Service is healthy";
    public static final String RESPONSE_200_BIO_PAGE_RETRIEVED = "Bio page retrieved successfully";
    // Error Response Descriptions
    public static final String RESPONSE_404_URL_NOT_FOUND      = "Short URL not found";
    public static final String RESPONSE_404_QR_NOT_FOUND       = "QR code not found";
    public static final String RESPONSE_404_BIO_PAGE_NOT_FOUND = "Bio page not found";
    public static final String RESPONSE_410_URL_INACTIVE       = "Short URL is no longer active";
    public static final String RESPONSE_410_QR_INACTIVE        = "QR code is no longer active";
    public static final String RESPONSE_403_QR_FORBIDDEN       = "Access to QR code is forbidden";
    public static final String RESPONSE_500_INTERNAL_ERROR     = "Internal server error";
    // ==================== SWAGGER PARAMETER CONSTANTS ====================
    public static final String PARAM_URL_KEY                   = "Short URL key";
    public static final String PARAM_QR_KEY                    = "Dynamic QR code key";
    public static final String PARAM_USERNAME                  = "Bio page username";
}
