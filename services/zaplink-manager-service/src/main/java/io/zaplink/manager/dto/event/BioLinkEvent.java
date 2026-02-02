package io.zaplink.manager.dto.event;

import io.zaplink.manager.dto.bio.BioLinkResponse;
import java.time.Instant;

public record BioLinkEvent( String eventType, String eventId, Instant timestamp, BioLinkResponse bioLink, Long pageId )
{
}
