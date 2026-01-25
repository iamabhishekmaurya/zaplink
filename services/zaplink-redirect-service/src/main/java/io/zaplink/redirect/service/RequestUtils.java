package io.zaplink.redirect.service;

import io.zaplink.redirect.common.enums.DeviceCategory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for extracting client information from HTTP requests.
 * Uses Java 21 pattern matching and enhanced switch expressions.
 */
public final class RequestUtils
{
    private RequestUtils()
    {
        // Utility class
    }

    /**
     * Extract client IP address from request, handling proxies.
     */
    public static String getClientIpAddress( HttpServletRequest request )
    {
        // Check X-Forwarded-For header (for proxies)
        String xForwardedFor = request.getHeader( "X-Forwarded-For" );
        if ( xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase( xForwardedFor ) )
        {
            return xForwardedFor.split( "," )[0].trim();
        }
        // Check X-Real-IP header
        String xRealIp = request.getHeader( "X-Real-IP" );
        if ( xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase( xRealIp ) )
        {
            return xRealIp;
        }
        // Fall back to remote address
        return request.getRemoteAddr();
    }

    /**
     * Extract device type from User-Agent header.
     * Uses pattern matching for cleaner code.
     */
    public static String extractDeviceType( String userAgent )
    {
        if ( userAgent == null )
            return "Unknown";
        String ua = userAgent.toLowerCase();
        // Using pattern matching style checks
        return switch ( detectDeviceCategory( ua ) )
        {
            case TABLET -> "Tablet";
            case MOBILE -> "Mobile";
            case DESKTOP -> "Desktop";
            default -> "Unknown";
        };
    }

    private static DeviceCategory detectDeviceCategory( String ua )
    {
        if ( ua.contains( "tablet" ) || ua.contains( "ipad" ) )
        {
            return DeviceCategory.TABLET;
        }
        if ( ua.contains( "mobile" ) || ua.contains( "android" ) || ua.contains( "iphone" ) )
        {
            return DeviceCategory.MOBILE;
        }
        if ( ua.contains( "mozilla" ) || ua.contains( "chrome" ) || ua.contains( "safari" )
                || ua.contains( "firefox" ) )
        {
            return DeviceCategory.DESKTOP;
        }
        return DeviceCategory.UNKNOWN;
    }

    /**
     * Extract browser name from User-Agent header.
     * Uses enhanced switch expression (Java 21).
     */
    public static String extractBrowser( String userAgent )
    {
        if ( userAgent == null )
            return "Unknown";
        String ua = userAgent.toLowerCase();
        // Enhanced switch expression with pattern-like matching
        return switch ( ua )
        {
            case String s when s.contains( "edg" ) -> "Edge";
            case String s when s.contains( "chrome" ) -> "Chrome";
            case String s when s.contains( "firefox" ) -> "Firefox";
            case String s when s.contains( "safari" ) -> "Safari";
            case String s when s.contains( "opera" ) || s.contains( "opr" ) -> "Opera";
            case String s when s.contains( "msie" ) || s.contains( "trident" ) -> "IE";
            default -> "Other";
        };
    }

    /**
     * Get referrer from request, with null handling.
     */
    public static String getReferrer( HttpServletRequest request )
    {
        String referrer = request.getHeader( "Referer" );
        return referrer != null ? referrer : "";
    }

    /**
     * Get User-Agent from request, with null handling.
     */
    public static String getUserAgent( HttpServletRequest request )
    {
        String userAgent = request.getHeader( "User-Agent" );
        return userAgent != null ? userAgent : "";
    }
}
