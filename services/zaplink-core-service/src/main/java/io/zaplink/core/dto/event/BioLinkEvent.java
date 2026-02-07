package io.zaplink.core.dto.event;

import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BioLinkEvent( @JsonProperty("event_type") String eventType,
                            @JsonProperty("event_id") String eventId,
                            Instant timestamp,
                            @JsonProperty("bio_link") BioLinkResponse bioLink,
                            @JsonProperty("page_id") Long pageId )
{
}
