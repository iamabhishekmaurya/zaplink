package io.zaplink.manager.service;

import org.springframework.stereotype.Service;

@Service
public interface UrlProvider {

    String getShortUrl( String key );
    
}
