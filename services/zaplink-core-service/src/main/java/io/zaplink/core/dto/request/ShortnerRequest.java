package io.zaplink.core.dto.request;

import java.util.List;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ShortnerRequest( @NotBlank(message = ErrorConstant.ORIGINAL_URL_REQUIRED) @Pattern(regexp = RegexConstant.URL_REGEX, message = ErrorConstant.URL_FORMAT_INVALID) String originalUrl,
                               @Size(max = 100, message = ErrorConstant.TITLE_LENGTH_EXCEEDED) String title,
                               @Size(max = 50, message = ErrorConstant.PLATFORM_LENGTH_EXCEEDED) String platform,
                               @Size(max = 5, message = ErrorConstant.TAGS_SIZE_EXCEEDED) List<@Size(max = 20, message = ErrorConstant.TAG_LENGTH_EXCEEDED) String> tags,
                               String traceId )
{
}
