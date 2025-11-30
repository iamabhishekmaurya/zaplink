package io.zaplink.shortner.dto.request;

import io.zaplink.shortner.common.constant.ErrorConstant;
import io.zaplink.shortner.common.constant.RegexConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
public class UrlConsumerRequest
    extends
    BaseRequest
{
    @NotBlank(message = ErrorConstant.SHORT_URL_REQUIRED) 
    @Pattern(regexp = RegexConstant.URL_REGEX, message = ErrorConstant.URL_FORMAT_INVALID) 
    private String shortUrlKey;
    @NotBlank(message = ErrorConstant.ORIGINAL_URL_REQUIRED) 
    @Pattern(regexp = RegexConstant.URL_REGEX, message = ErrorConstant.URL_FORMAT_INVALID) 
    private String originalUrl;
}
