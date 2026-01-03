package io.zaplink.core.service.shortner;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;

public interface UrlShortnerService
{
    ShortnerResponse shortUrl( ShortnerRequest urlRequest, String userEmail );
}
