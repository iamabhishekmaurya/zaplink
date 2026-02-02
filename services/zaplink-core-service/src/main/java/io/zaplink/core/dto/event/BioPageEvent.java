package io.zaplink.core.dto.event;

import io.zaplink.core.dto.response.biopage.BioPageResponse;
import java.time.Instant;

public record BioPageEvent( String eventType, String eventId, Instant timestamp, BioPageResponse bioPage )
{
}
