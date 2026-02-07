package io.zaplink.core.dto.request.dynamicqr;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.dto.request.RedirectRuleDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDestinationRequest( @JsonProperty("destination_url") @NotBlank(message = ErrorConstant.VALIDATION_DESTINATION_URL_REQUIRED) @Size(max = 2048, message = ErrorConstant.VALIDATION_DESTINATION_URL_MAX_LENGTH) String destinationUrl,
                                        List<RedirectRuleDto> rules )
{
}
