package io.zaplink.redirect.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * GeoIP service for IP address geolocation using MaxMind GeoLite2.
 * Optimized for local lookup without external API calls.
 */
@Service @Slf4j
public class GeoIpService
{
    @Value("${geoip.db-path:classpath:GeoLite2-City.mmdb}")
    private String         dbPath;
    private DatabaseReader dbReader;
    @PostConstruct
    public void init()
    {
        try
        {
            File database = new File( dbPath );
            if ( database.exists() )
            {
                dbReader = new DatabaseReader.Builder( database ).build();
                log.info( "GeoIP Database loaded from: {}", dbPath );
            }
            else
            {
                // Try loading from classpath if file path not found (for Docker/Jar)
                try (var stream = getClass().getClassLoader().getResourceAsStream( "GeoLite2-City.mmdb" ))
                {
                    if ( stream != null )
                    {
                        dbReader = new DatabaseReader.Builder( stream ).build();
                        log.info( "GeoIP Database loaded from classpath" );
                    }
                    else
                    {
                        log.warn( "GeoIP Database not found at {} or in classpath. GeoIP features will be disabled.",
                                  dbPath );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            log.error( "Failed to initialize GeoIP Database", e );
        }
    }

    @PreDestroy
    public void cleanup()
    {
        if ( dbReader != null )
        {
            try
            {
                dbReader.close();
            }
            catch ( IOException e )
            {
                log.error( "Error closing GeoIP reader", e );
            }
        }
    }

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
        // Skip check for private IPs for performance if needed, but MaxMind handles them gracefully (returns null)
        if ( isLocalOrPrivate( ipAddress ) )
        {
            return location;
        }
        if ( dbReader == null )
        {
            return location;
        }
        try
        {
            InetAddress ip = InetAddress.getByName( ipAddress );
            CityResponse response = dbReader.city( ip );
            Country country = response.getCountry();
            City city = response.getCity();
            if ( country != null && country.getIsoCode() != null )
            {
                location.put( "country", country.getIsoCode() );
            }
            if ( city != null && city.getName() != null )
            {
                location.put( "city", city.getName() );
            }
        }
        catch ( Exception e )
        {
            log.trace( "Failed to resolve location for {}: {}", ipAddress, e.getMessage() );
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
