package io.zaplink.processor.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.zaplink.processor.service.GeoIpService;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class ExternalApiGeoIpService
    implements
    GeoIpService
{
    private final RestTemplate  restTemplate = new RestTemplate();
    private static final String API_URL      = "http://ip-api.com/json/{ip}";
    @Override
    public Map<String, String> resolveLocation( String ipAddress )
    {
        Map<String, String> location = new HashMap<>();
        try
        {
            // Handle localhost and private IPs
            if ( isLocalOrPrivate( ipAddress ) )
            {
                log.debug( "Skipping GeoIP for local/private IP: {}", ipAddress );
                return location;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject( API_URL, Map.class, ipAddress );
            if ( response != null && "success".equals( response.get( "status" ) ) )
            {
                location.put( "country", (String) response.get( "country" ) );
                location.put( "city", (String) response.get( "city" ) );
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

    private boolean isLocalOrPrivate( String ip )
    {
        return ip == null || ip.equals( "127.0.0.1" ) || ip.equals( "0:0:0:0:0:0:0:1" ) || ip.startsWith( "192.168." )
                || ip.startsWith( "10." );
    }
}
