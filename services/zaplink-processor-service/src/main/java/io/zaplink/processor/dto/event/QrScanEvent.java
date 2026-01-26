package io.zaplink.processor.dto.event;

import java.time.Instant;

/**
 * QR scan event record from Kafka.
 * Matches the producer in zaplink-redirect-service.
 *
 * @param qrKey       the dynamic QR key
 * @param ipAddress   client IP address
 * @param userAgent   browser user agent
 * @param referrer    HTTP referer header
 * @param country     resolved country from GeoIP
 * @param city        resolved city from GeoIP
 * @param deviceType  extracted device type
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
}
