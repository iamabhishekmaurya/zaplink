package io.zaplink.redirect.common.constants;

/**
 * Kafka topic names for analytics events.
 */
public final class KafkaTopics
{
    private KafkaTopics()
    {
        // Utility class
    }
    public static final String URL_CLICK_EVENTS = "url-click-events";
    public static final String QR_SCAN_EVENTS   = "qr-scan-events";
}
