package io.zaplink.shortner.service;

import io.zaplink.shortner.dto.request.ShortnerRequest;
import io.zaplink.shortner.dto.response.ShortnerResponse;

public interface UrlShortnerService
{
    ShortnerResponse shortUrl( ShortnerRequest urlRequest );
}
