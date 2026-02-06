package io.zaplink.media.dto.error;

import java.util.List;

public record ErrorResponse( String timestamp,
                             String status,
                             String message,
                             String path,
                             List<FieldError> fieldErrors )
{
}
