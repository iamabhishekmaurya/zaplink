package io.zaplink.shortner.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShortnerResponse
    extends
    BaseResponse
{
    private String url;
}
