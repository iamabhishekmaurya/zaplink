package io.zaplink.redirect.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.redirect.common.enums.RuleDimension;
import lombok.Builder;

/**
 * Cacheable DTO containing the link profile (Original URL + Rules).
 */
@Builder
public record RedirectConfigDto( @JsonProperty("original_url") String originalUrl, List<RedirectRuleDto> rules )
{
    @Builder
    public record RedirectRuleDto( RuleDimension dimension,
                                   String value,
                                   @JsonProperty("destination_url") String destinationUrl,
                                   int priority )
    {
    }
}
