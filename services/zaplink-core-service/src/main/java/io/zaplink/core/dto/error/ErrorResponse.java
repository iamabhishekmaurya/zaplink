package io.zaplink.core.dto.error;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse( String timestamp,
                             String status,
                             String message,
                             String path,
                             @JsonProperty("field_errors") List<FieldError> fieldErrors )
{
}
