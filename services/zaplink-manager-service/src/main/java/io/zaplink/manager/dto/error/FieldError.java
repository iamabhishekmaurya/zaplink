package io.zaplink.manager.dto.error;

public record FieldError(
    String field,
    String message,
    String rejectedValue
) {}