package io.zaplink.manager.service;

import java.util.Map;

public interface GeoIpService
{
    Map<String, String> resolveLocation( String ipAddress );
}
