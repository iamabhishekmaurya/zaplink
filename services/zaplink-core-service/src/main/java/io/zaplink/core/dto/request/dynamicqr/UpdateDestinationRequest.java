package io.zaplink.core.dto.request.dynamicqr;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDestinationRequest {
    
    @NotBlank(message = "Destination URL is required")
    @Size(max = 2048, message = "Destination URL must not exceed 2048 characters")
    private String destinationUrl;
}
