package io.zaplink.core.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FieldError
{
    private String field;
    private String message;
    private String rejectedValue;
}