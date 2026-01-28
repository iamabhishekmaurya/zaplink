package io.zaplink.core.service.shortner;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.dto.request.UpdateShortLinkRequest;

public interface UrlShortnerService
{
    ShortnerResponse createShortUrl( ShortnerRequest urlRequest, String userEmail );

    ShortnerResponse updateShortUrl( UpdateShortLinkRequest updateRequest, String userEmail );
}
