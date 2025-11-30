package io.zaplink.shortner.dto.error;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ErrorResponse
{
    private String           timestamp;
    private String           status;
    private String           message;
    private String           path;
    private List<FieldError> fieldErrors;
}
