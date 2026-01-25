package io.zaplink.redirect.dto.event;

import java.time.Instant;

/**
 * QR scan event record for Kafka publishing.
 * Uses Java 21 record for immutable event data.
 *
 * @param qrKey       the dynamic QR key
 * @param ipAddress   client IP address
 * @param userAgent   browser user agent
 * @param referrer    HTTP referer header
 * @param country     resolved country from GeoIP
 * @param city        resolved city from GeoIP
 * @param deviceType  extracted device type (Mobile/Desktop/Tablet)
 * @param browser     extracted browser name
 * @param timestamp   event timestamp
 * @param traceId     distributed tracing ID
 */
public record QrScanEvent( String qrKey,
                           String ipAddress,
                           String userAgent,
                           String referrer,
                           String country,
                           String city,
                           String deviceType,
                           String browser,
                           Instant timestamp,
                           String traceId )
{
    /**
     * Builder-style factory method for creating QR scan events.
     */
    public static QrScanEvent of( String qrKey,
                                  String ipAddress,
                                  String userAgent,
                                  String referrer,
                                  String country,
                                  String city,
                                  String deviceType,
                                  String browser,
                                  String traceId )
    {
        return new QrScanEvent( qrKey,
                                ipAddress,
                                userAgent,
                                referrer,
                                country,
                                city,
                                deviceType,
                                browser,
                                Instant.now(),
                                traceId );
    }
}
