package io.zaplink.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.manager.common.enums.RuleDimension;

public record RedirectRuleDto( RuleDimension dimension,
                               String value,
                               @JsonProperty("destination_url") String destinationUrl,
                               Integer priority )
{
}
