package io.zaplink.core.dto.error;

public record FieldError( String field, String message, String rejectedValue )
{
}