package io.zaplink.core.dto.request.dynamicqr;

import java.util.List;

import io.zaplink.core.dto.request.RedirectRuleDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDestinationRequest(
    @NotBlank(message = "Destination URL is required") @Size(max = 2048, message = "Destination URL must not exceed 2048 characters")
    String destinationUrl,
    
    List<RedirectRuleDto> rules
) {
}
