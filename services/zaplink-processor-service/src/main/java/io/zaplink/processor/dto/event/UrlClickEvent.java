package io.zaplink.processor.dto.event;

import java.time.Instant;

/**
 * URL click event record from Kafka.
 * Matches the producer in zaplink-redirect-service.
 *
 * @param urlKey      the short URL key
 * @param ipAddress   client IP address
 * @param userAgent   browser user agent
 * @param referrer    HTTP referer header
 * @param country     resolved country from GeoIP (if already resolved by producer, but usually null)
 * @param city        resolved city from GeoIP
 * @param deviceType  extracted device type
 * @param browser     extracted browser name
 * @param timestamp   event timestamp
 * @param traceId     distributed tracing ID
 */
public record UrlClickEvent( String urlKey,
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
