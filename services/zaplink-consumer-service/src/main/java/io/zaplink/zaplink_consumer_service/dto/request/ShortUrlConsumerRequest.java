package io.zaplink.zaplink_consumer_service.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ShortUrlConsumerRequest
    extends
    BaseRequest
{
    private String shortUrlKey;
    private String originalUrl;
}
