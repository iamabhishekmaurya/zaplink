package io.zaplink.shortner.dto.request;

import io.zaplink.shortner.common.constant.ErrorConstant;
import io.zaplink.shortner.common.constant.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
public class ShortnerRequest
    extends
    BaseRequest
{
    @NotBlank(message = ErrorConstant.ORIGINAL_URL_REQUIRED) 
    @Pattern(regexp = RegexConstant.URL_REGEX, message = ErrorConstant.URL_FORMAT_INVALID)
    private String originalUrl;
}
