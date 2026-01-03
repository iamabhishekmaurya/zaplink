package io.zaplink.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.service.shortner.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("${api.base-path}/short")
public class CoreController
{
    private final UrlShortnerService urlServiceProvider;
    @PostMapping("/url")
    public ShortnerResponse shortUrl( @Valid @RequestBody ShortnerRequest urlRequest,
                                      @RequestHeader(value = "X-User-Email", required = false) String userEmail )
    {
        return urlServiceProvider.shortUrl( urlRequest, userEmail );
    }
}
