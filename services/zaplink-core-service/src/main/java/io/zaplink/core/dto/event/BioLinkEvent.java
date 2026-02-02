package io.zaplink.core.dto.event;

import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import java.time.Instant;

public record BioLinkEvent( String eventType, String eventId, Instant timestamp, BioLinkResponse bioLink, Long pageId )
{
}
