package io.zaplink.zaplink_producer_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.zaplink.zaplink_producer_service.dto.request.ShortUrlRequest;
import io.zaplink.zaplink_producer_service.dto.response.ShortUrlResponse;
import io.zaplink.zaplink_producer_service.service.provider.UrlServiceProvider;

@RestController @RequestMapping("/producers")
public class ZaplinkProducerController
{
    private UrlServiceProvider urlServiceProvider;
    public ZaplinkProducerController( UrlServiceProvider urlServiceProvider )
    {
        this.urlServiceProvider = urlServiceProvider;
    }

    @PostMapping("/short/url")
    public ShortUrlResponse shortUrl( @RequestBody() ShortUrlRequest urlRequest )
    {
        return urlServiceProvider.shortUrl( urlRequest );
    }
    @GetMapping("/{key}")
    public RedirectView getValue( @PathVariable("key") String key )
    {
        ShortUrlResponse shortUrlResponse = urlServiceProvider.getShortUrl( key );
        return new RedirectView(shortUrlResponse.getUrl());

    }
}
