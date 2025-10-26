package io.zaplink.zaplink_producer_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShortUrlResponse
    extends
    BaseResponse
{
    private String url;
}
