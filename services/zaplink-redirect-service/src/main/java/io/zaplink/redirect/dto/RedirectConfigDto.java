package io.zaplink.redirect.dto;

import java.util.List;

import io.zaplink.redirect.common.enums.RuleDimension;
import lombok.Builder;

/**
 * Cacheable DTO containing the link profile (Original URL + Rules).
 */
@Builder
public record RedirectConfigDto( String originalUrl, List<RedirectRuleDto> rules )
{
    @Builder
    public record RedirectRuleDto( RuleDimension dimension, String value, String destinationUrl, int priority )
    {
    }
}
