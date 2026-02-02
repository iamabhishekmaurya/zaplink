package io.zaplink.manager.common.client;

import io.zaplink.manager.dto.bio.BioPageResponse;
import io.zaplink.manager.dto.OrganizationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CoreClient
{
    private final RestTemplate restTemplate;
    private final String       coreServiceUrl;
    public CoreClient( RestTemplate restTemplate, @Value("${zaplink.services.core.url}") String coreServiceUrl )
    {
        this.restTemplate = restTemplate;
        this.coreServiceUrl = coreServiceUrl;
    }

    public BioPageResponse getBioPage( Long bioPageId )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/bio-pages/{id}" )
                .buildAndExpand( bioPageId ).toUriString();
        return restTemplate.getForObject( url, BioPageResponse.class );
    }

    public OrganizationDto getOrganization( Long organizationId )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/organizations/{id}" )
                .buildAndExpand( organizationId ).toUriString();
        return restTemplate.getForObject( url, OrganizationDto.class );
    }

    public boolean organizationExists( Long organizationId )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/organizations/{id}/exists" )
                .buildAndExpand( organizationId ).toUriString();
        try
        {
            Boolean result = restTemplate.getForObject( url, Boolean.class );
            return result != null && result;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public boolean bioPageExists( Long bioPageId )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/bio-pages/{id}/exists" )
                .buildAndExpand( bioPageId ).toUriString();
        try
        {
            Boolean result = restTemplate.getForObject( url, Boolean.class );
            return result != null && result;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public BioPageResponse createBioPage( BioPageResponse bioPage )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/bio-pages" ).build()
                .toUriString();
        return restTemplate.postForObject( url, bioPage, BioPageResponse.class );
    }

    public OrganizationDto createOrganization( OrganizationDto organization )
    {
        String url = UriComponentsBuilder.fromUriString( coreServiceUrl ).path( "/api/organizations" ).build()
                .toUriString();
        return restTemplate.postForObject( url, organization, OrganizationDto.class );
    }
}
