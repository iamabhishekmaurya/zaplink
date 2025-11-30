package io.zaplink.shortner.dto.request;

import io.zaplink.shortner.common.constant.ErrorConstant;
import io.zaplink.shortner.common.constant.RegexConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class BaseRequest
{
    @NotNull 
    @Size(max = 32, message = ErrorConstant.TRACE_ID_LENGTH_ERROR) 
    @Pattern(regexp = RegexConstant.TRACE_ID_REGEX, message = ErrorConstant.TRACE_ID_CONTAINS_ALPHANUMERIC_AND_HYPHAN)
    private String traceId;
}