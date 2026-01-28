package io.zaplink.manager.dto;

import io.zaplink.manager.common.enums.RuleDimension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RedirectRuleDto
{
    private RuleDimension dimension;
    private String        value;
    private String        destinationUrl;
    private Integer       priority;
}
