package io.zaplink.processor.service;

import java.util.Map;

public interface GeoIpService
{
    /**
     * Resolves location information for a given IP address.
     * @param ipAddress The IP address to resolve.
     * @return A map containing location data (e.g., country, city).
     */
    Map<String, String> resolveLocation( String ipAddress );
}
