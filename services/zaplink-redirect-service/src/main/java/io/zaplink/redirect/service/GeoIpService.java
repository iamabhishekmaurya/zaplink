package io.zaplink.redirect.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * GeoIP service for IP address geolocation.
 * Uses external API to resolve country and city from IP.
 */
@Service @Slf4j
public class GeoIpService
{
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${geoip.api-url:http://ip-api.com/json/{ip}}")
    private String             apiUrl;
    /**
     * Resolve location from IP address.
     *
     * @param ipAddress the IP address to resolve
     * @return Map containing country and city
     */
    public Map<String, String> resolveLocation( String ipAddress )
    {
        Map<String, String> location = new HashMap<>();
        location.put( "country", "Unknown" );
        location.put( "city", "Unknown" );
        try
        {
            // Skip local/private IPs
            if ( isLocalOrPrivate( ipAddress ) )
            {
                log.debug( "Skipping GeoIP for local/private IP: {}", ipAddress );
                return location;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject( apiUrl, Map.class, ipAddress );
            if ( response != null && "success".equals( response.get( "status" ) ) )
            {
                location.put( "country", (String) response.getOrDefault( "country", "Unknown" ) );
                location.put( "city", (String) response.getOrDefault( "city", "Unknown" ) );
                log.debug( "Resolved location for {}: {}, {}", ipAddress, location.get( "city" ),
                           location.get( "country" ) );
            }
            else
            {
                log.warn( "Failed to resolve location for {}: {}", ipAddress, response );
            }
        }
        catch ( Exception e )
        {
            log.error( "Error calling GeoIP API for {}: {}", ipAddress, e.getMessage() );
        }
        return location;
    }

    /**
     * Check if IP is local or private.
     */
    private boolean isLocalOrPrivate( String ip )
    {
        return ip == null || ip.equals( "127.0.0.1" ) || ip.equals( "0:0:0:0:0:0:0:1" ) || ip.equals( "::1" )
                || ip.startsWith( "192.168." ) || ip.startsWith( "10." ) || ip.startsWith( "172.16." )
                || ip.startsWith( "172.17." ) || ip.startsWith( "172.18." ) || ip.startsWith( "172.19." )
                || ip.startsWith( "172.2" ) || ip.startsWith( "172.30." ) || ip.startsWith( "172.31." );
    }
}
