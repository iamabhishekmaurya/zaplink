package io.zaplink.manager.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.service.UrlManagerService;

@RestController @RequestMapping("/manager")
public class UrlController
{
    private final UrlManagerService urlProvider;
    public UrlController( UrlManagerService urlProvider )
    {
        this.urlProvider = urlProvider;
    }

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
    public RedirectView getValue( @PathVariable("key") String key )
    {
        return new RedirectView( urlProvider.getShortUrl( key ) );
    }
}
