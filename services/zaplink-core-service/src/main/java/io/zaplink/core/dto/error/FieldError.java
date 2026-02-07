package io.zaplink.core.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FieldError( String field, String message, @JsonProperty("rejected_value") String rejectedValue )
{
}