package io.zaplink.manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.zaplink.manager.service.UrlProvider;

@RestController
public class UrlController
{
    private UrlProvider urlProvider;
    public UrlController( UrlProvider urlProvider )
    {
        this.urlProvider = urlProvider;
    }

    @GetMapping("/{key}")
    public RedirectView getValue( @PathVariable("key") String key )
    {
        return new RedirectView( urlProvider.getShortUrl( key ) );
    }
}
