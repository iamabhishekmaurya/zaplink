package io.zaplink.core.dto.request;

import io.zaplink.core.common.enums.RuleDimension;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RedirectRuleDto( @NotNull(message = "Dimension is required") RuleDimension dimension,
                               @NotBlank(message = "Value is required") String value,
                               @NotBlank(message = "Destination URL is required") String destinationUrl,
                               @NotNull(message = "Priority is required") Integer priority )
{
}
