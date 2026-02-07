package io.zaplink.core.dto.request;

import io.zaplink.core.common.enums.RuleDimension;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;

public record RedirectRuleDto( @NotNull(message = ErrorConstant.VALIDATION_DIMENSION_REQUIRED) RuleDimension dimension,
                               @NotBlank(message = ErrorConstant.VALIDATION_VALUE_REQUIRED) String value,
                               @JsonProperty("destination_url") @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) String destinationUrl,
                               @NotNull(message = ErrorConstant.VALIDATION_PRIORITY_REQUIRED) Integer priority )
{
}
