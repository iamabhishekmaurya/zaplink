package io.zaplink.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BaseRequest
{
    private String traceId;
}