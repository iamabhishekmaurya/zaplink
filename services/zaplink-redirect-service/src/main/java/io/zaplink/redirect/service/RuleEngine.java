package io.zaplink.redirect.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.zaplink.redirect.entity.RedirectRuleEntity;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * Deterministic engine for evaluating routing rules.
 */
@Component @Slf4j
public class RuleEngine
{
    /**
     * Context for rule evaluation.
     */
    @Builder
    public record RoutingContext( String deviceType, // e.g., 'Mobile', 'Desktop'
                                  String os, // e.g., 'iOS', 'Android', 'Windows'
                                  String country // e.g., 'US', 'UK'
    )
    {
    }
    /**
     * Evaluate rules against the context to find the best matching destination.
     * Rules are expected to be pre-sorted by priority.
     *
     * @param rules   Sorted list of rules
     * @param context Request context
     * @return Optional destination URL if a match is found
     */
    public Optional<String> evaluate( List<RedirectRuleEntity> rules, RoutingContext context )
    {
        if ( rules == null || rules.isEmpty() )
        {
            return Optional.empty();
        }
        for ( RedirectRuleEntity rule : rules )
        {
            if ( matches( rule, context ) )
            {
                log.debug( "Rule matched: {} -> {}", rule.getDimension(), rule.getDestinationUrl() );
                return Optional.of( rule.getDestinationUrl() );
            }
        }
        return Optional.empty();
    }

    private boolean matches( RedirectRuleEntity rule, RoutingContext context )
    {
        String ruleValue = rule.getValue();
        if ( ruleValue == null )
            return false;
        return switch ( rule.getDimension() )
        {
            case DEVICE_TYPE -> ruleValue.equalsIgnoreCase( context.deviceType() );
            case OS -> ruleValue.equalsIgnoreCase( context.os() );
            case COUNTRY -> ruleValue.equalsIgnoreCase( context.country() );
        };
    }
}
