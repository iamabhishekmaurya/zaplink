package io.zaplink.shortner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.shortner.dto.request.ShortnerRequest;
import io.zaplink.shortner.dto.response.ShortnerResponse;
import io.zaplink.shortner.service.UrlShortnerService;

@RestController @RequestMapping("/producers")
public class ZaplinkShortnerController
{
    private UrlShortnerService urlServiceProvider;
    public ZaplinkShortnerController( UrlShortnerService urlServiceProvider )
    {
        this.urlServiceProvider = urlServiceProvider;
    }

    @PostMapping("/short/url")
    public ShortnerResponse shortUrl( @RequestBody() ShortnerRequest urlRequest )
    {
        return urlServiceProvider.shortUrl( urlRequest );
    }

    @GetMapping("/short/url/{key}")
    public ShortnerResponse getShortUrl( @PathVariable() String key )
    {
        return urlServiceProvider.getShortUrl( key );
    }
}
