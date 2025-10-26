package io.zaplink.zaplink_producer_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShortUrlRequest
    extends
    BaseRequest
{
    private String longUrl;
}
