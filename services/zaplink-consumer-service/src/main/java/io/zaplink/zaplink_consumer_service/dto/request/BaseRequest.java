package io.zaplink.zaplink_consumer_service.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BaseRequest
{
    private String traceId;
}