package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.zaplink.manager.dto.request.AnalyticsEvent;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.service.UrlManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping("/manager")
public class UrlController
{
    private final UrlManagerService urlProvider;
    @GetMapping("/links")
    public List<LinkResponse> getLinks( @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlProvider.getLinksByUser( userEmail );
    }

    @GetMapping("/stats")
    public StatsResponse getStats( @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlProvider.getUserStats( userEmail );
    }

    @DeleteMapping("/links/{id}")
    public void deleteLink( @PathVariable("id") Long id,
                            @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        urlProvider.deleteLink( id, userEmail );
    }

    @GetMapping("/{key}")
    public RedirectView getValue( @PathVariable String key, HttpServletRequest request )
    {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().urlKey( key ).ipAddress( request.getRemoteAddr() )
                .userAgent( request.getHeader( "User-Agent" ) ).referrer( request.getHeader( "Referer" ) )
                .timestamp( java.time.LocalDateTime.now() ).build();
        return new RedirectView( urlProvider.getShortUrl( analyticsEvent ) );
    }
}
