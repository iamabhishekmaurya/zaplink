package io.zaplink.processor.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UrlConsumerRequest
    extends
    BaseRequest
{
    private String shortUrlKey;
    private String originalUrl;
}
