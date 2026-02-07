package io.zaplink.core.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShortnerResponse( String url, @JsonProperty("trace_id") String traceId, List<String> tags )
{
}
