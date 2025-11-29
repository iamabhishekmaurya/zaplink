package io.zaplink.shortner.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ShortnerRequest
    extends
    BaseRequest
{
    private String longUrl;
}
