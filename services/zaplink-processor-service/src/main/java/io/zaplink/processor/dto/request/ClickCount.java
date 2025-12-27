package io.zaplink.processor.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ClickCount
    extends
    BaseRequest
{
    private String urlKey;
    private int    count;
}
