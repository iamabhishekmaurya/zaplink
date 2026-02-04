package io.zaplink.manager.dto.event;

import java.time.Instant;

/**
 * DTO for campaign assignment events consumed from Kafka.
 * Represents the structure of campaign assignment events from Core Service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record CampaignAssignmentEvent(
                                       /**
                                        * Event type identifier.
                                        */
                                       String eventType,
                                       /**
                                        * Unique event ID for tracing.
                                        */
                                       String eventId,
                                       /**
                                        * Timestamp when the event was created.
                                        */
                                       Instant timestamp,
                                       /**
                                        * Assignment ID (primary key for the influencer campaign view).
                                        */
                                       Long assignmentId,
                                       /**
                                        * Campaign ID.
                                        */
                                       Long campaignId,
                                       /**
                                        * Campaign name.
                                        */
                                       String campaignName,
                                       /**
                                        * Campaign description.
                                        */
                                       String campaignDescription,
                                       /**
                                        * Campaign status.
                                        */
                                       String campaignStatus,
                                       /**
                                        * Campaign start date.
                                        */
                                       Instant startDate,
                                       /**
                                        * Campaign end date.
                                        */
                                       Instant endDate,
                                       /**
                                        * Team member ID (influencer).
                                        */
                                       Long teamMemberId,
                                       /**
                                        * Team ID.
                                        */
                                       Long teamId,
                                       /**
                                        * Team name.
                                        */
                                       String teamName,
                                       /**
                                        * Organization ID.
                                        */
                                       Long organizationId,
                                       /**
                                        * Organization name.
                                        */
                                       String organizationName,
                                       /**
                                        * Assignment status.
                                        */
                                       String assignmentStatus,
                                       /**
                                        * When the influencer was assigned to the campaign.
                                        */
                                       Instant assignedAt,
                                       /**
                                        * When the influencer completed the campaign (if applicable).
                                        */
                                       Instant completedAt )
{
}
