package io.zaplink.manager.common.client;

import io.zaplink.manager.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuthClient {
    
    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    
    public AuthClient(RestTemplate restTemplate, @Value("${zaplink.services.auth.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }
    
    public UserDto getUser(Long userId) {
        String url = UriComponentsBuilder.fromUriString(authServiceUrl)
                .path("/api/users/{id}")
                .buildAndExpand(userId)
                .toUriString();
        
        return restTemplate.getForObject(url, UserDto.class);
    }
    
    public UserDto getUserByEmail(String email) {
        String url = UriComponentsBuilder.fromUriString(authServiceUrl)
                .path("/api/users/by-email/{email}")
                .buildAndExpand(email)
                .toUriString();
        
        return restTemplate.getForObject(url, UserDto.class);
    }
    
    public boolean validateToken(String token) {
        String url = UriComponentsBuilder.fromUriString(authServiceUrl)
                .path("/api/auth/validate")
                .queryParam("token", token)
                .build()
                .toUriString();
        
        try {
            Boolean result = restTemplate.getForObject(url, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean userExists(Long userId) {
        String url = UriComponentsBuilder.fromUriString(authServiceUrl)
                .path("/api/users/{id}/exists")
                .buildAndExpand(userId)
                .toUriString();
        
        try {
            Boolean result = restTemplate.getForObject(url, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            return false;
        }
    }
}
