package io.zaplink.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.service.UrlShortnerService;
import jakarta.validation.Valid;

@RestController @RequestMapping("/shortner")
public class CoreController
{
    private final UrlShortnerService urlServiceProvider;
    public CoreController( UrlShortnerService urlServiceProvider )
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
