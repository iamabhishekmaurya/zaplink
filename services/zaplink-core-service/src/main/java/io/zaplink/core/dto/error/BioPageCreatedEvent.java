package io.zaplink.core.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Record representing a bio page creation event for event-driven architecture.
* 
* <p>This immutable record carries information about newly created
* bio pages through the event system. It enables loose coupling
* between the creation service and downstream processors.</p>
* 
* <p><strong>Event Usage:</strong></p>
* <ul>
*   <li>Notification systems (email, SMS, push)</li>
*   <li>Analytics and tracking</li>
*   <li>Search index updates</li>
*   <li>Cache warming strategies</li>
*   <li>Audit logging</li>
* </ul>
* 
* <p><strong>Event Characteristics:</strong></p>
* <ul>
*   <li>Immutable for thread safety</li>
*   <li>Contains essential page identification data</li>
*   <li>Lightweight for efficient event processing</li>
* </ul>
* 
* @param id the unique identifier of the created bio page
* @param username the username of the created bio page
* @param ownerId the owner identifier of the created bio page
*/
public record BioPageCreatedEvent( Long id, String username, @JsonProperty("owner_id") String ownerId )
{
}
