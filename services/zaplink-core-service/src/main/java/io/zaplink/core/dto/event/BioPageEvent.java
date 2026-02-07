package io.zaplink.core.dto.event;

import io.zaplink.core.dto.response.biopage.BioPageResponse;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BioPageEvent( @JsonProperty("event_type") String eventType,
                            @JsonProperty("event_id") String eventId,
                            Instant timestamp,
                            @JsonProperty("bio_page") BioPageResponse bioPage )
{
}
