package io.zaplink.media.dto.error;

public record FieldError( String field, String message, String rejectedValue )
{
}
