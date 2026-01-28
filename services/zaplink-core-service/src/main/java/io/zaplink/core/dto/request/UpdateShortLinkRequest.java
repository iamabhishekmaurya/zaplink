package io.zaplink.core.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.zaplink.core.common.constants.ErrorConstant;

public record UpdateShortLinkRequest( @NotBlank(message = "Short URL Key is required") String shortUrlKey,
                                      @Size(max = 100, message = ErrorConstant.TITLE_LENGTH_EXCEEDED) String title,
                                      @Size(max = 50, message = ErrorConstant.PLATFORM_LENGTH_EXCEEDED) String platform,
                                      @Size(max = 5, message = ErrorConstant.TAGS_SIZE_EXCEEDED) List<@Size(max = 20, message = ErrorConstant.TAG_LENGTH_EXCEEDED) String> tags,
                                      List<RedirectRuleDto> rules )
{
}
