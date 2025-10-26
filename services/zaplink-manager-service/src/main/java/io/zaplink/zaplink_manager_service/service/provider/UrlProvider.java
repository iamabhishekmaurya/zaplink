package io.zaplink.zaplink_manager_service.service.provider;

import org.springframework.stereotype.Service;

@Service
public interface UrlProvider {

    String getShortUrl( String key );
    
}
