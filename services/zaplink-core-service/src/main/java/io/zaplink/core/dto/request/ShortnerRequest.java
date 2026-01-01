package io.zaplink.core.dto.request;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ShortnerRequest
    extends
    BaseRequest
{
    @NotBlank(message = ErrorConstant.ORIGINAL_URL_REQUIRED) @Pattern(regexp = RegexConstant.URL_REGEX, message = ErrorConstant.URL_FORMAT_INVALID)
    private String originalUrl;
}
