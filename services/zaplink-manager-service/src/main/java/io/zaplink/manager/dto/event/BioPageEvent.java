package io.zaplink.manager.dto.event;

import io.zaplink.manager.dto.bio.BioPageResponse;
import java.time.Instant;

public record BioPageEvent( String eventType, String eventId, Instant timestamp, BioPageResponse bioPage )
{
}
