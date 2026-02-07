package io.zaplink.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BaseResponse( @JsonProperty("trace_id") String traceId )
{
}
