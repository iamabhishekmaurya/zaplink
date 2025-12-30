package io.zaplink.shortner.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.shortner.dto.request.ShortnerRequest;
import io.zaplink.shortner.dto.response.ShortnerResponse;
import io.zaplink.shortner.service.UrlShortnerService;
import jakarta.validation.Valid;

@RestController @RequestMapping("/shortner")
public class ZaplinkShortnerController
{
    private final UrlShortnerService urlServiceProvider;
    public ZaplinkShortnerController( UrlShortnerService urlServiceProvider )
    {
        this.urlServiceProvider = urlServiceProvider;
    }

    @PostMapping("/short/url")
    public ShortnerResponse shortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                      @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlServiceProvider.shortUrl( urlRequest, userEmail );
    }
}
