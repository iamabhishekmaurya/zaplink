package io.zaplink.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BaseRequest( @JsonProperty("trace_id") String traceId )
{
}