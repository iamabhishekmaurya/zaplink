package io.zaplink.core.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.zaplink.core.common.constants.ErrorConstant;

public record UpdateShortLinkRequest( @JsonProperty("short_url_key") @NotBlank(message = ErrorConstant.VALIDATION_SHORT_URL_KEY_REQUIRED) String shortUrlKey,
                                      @Size(max = 100, message = ErrorConstant.TITLE_LENGTH_EXCEEDED) String title,
                                      @Size(max = 50, message = ErrorConstant.PLATFORM_LENGTH_EXCEEDED) String platform,
                                      List<String> tags,
                                      List<RedirectRuleDto> rules )
{
}
