package io.zaplink.scheduler.common.constants;

/**
 * Centralized Log Messages.
 * <p>
 * Contains structured logging templates for the Scheduler Service.
 * Used with SLF4J placeholders (e.g., {}).
 * </p>
 */
public final class LogMessages
{
    private LogMessages()
    {
    }
    // Scheduler Service Logs
    public static final String SCHEDULING_POST         = "Scheduling post {} for time {}";
    public static final String RESCHEDULING_POST       = "Rescheduling post {} to time {}";
    public static final String ENTERING_SCHEDULE_POST  = "Entering schedulePost with post: {}";
    public static final String VALIDATION_FAILED_PAST  = "Validation failed: Attempted to schedule post in the past: {}";
    public static final String RETRIEVING_POSTS        = "Retrieving posts for owner: {} between {} and {}";
    public static final String ENTERING_UPDATE_TIME    = "Entering updateScheduledTime for postId: {} with newTime: {}";
    public static final String POST_NOT_FOUND_UPDATE   = "Post not found while attempting update: {}";
    public static final String RESCHEDULE_PUBLISHED    = "Attempt to reschedule published post: {}";
    public static final String RESCHEDULE_PAST_TIME    = "Validation failed: Attempted to reschedule post to past time: {}";
    public static final String QUARTZ_JOB_SCHEDULED    = "Quartz job scheduled successfully for post: {}";
    public static final String QUARTZ_JOB_RESCHEDULED  = "Quartz job rescheduled successfully for post: {}";
    public static final String TRIGGER_NOT_FOUND       = "Trigger not found for rescheduling, creating new job for post: {}";
    // Controller Logs
    public static final String API_SCHEDULE_POST       = "API Request: Schedule post for user: {}";
    public static final String API_GET_POSTS           = "API Request: Get posts for user: {} range: {} to {}";
    public static final String API_UPDATE_TIME         = "API Request: Update time for post: {}";
    // Job Logs
    public static final String EXECUTE_PUBLISH_JOB     = "Executing PostPublishJob for postId: {}";
    public static final String POST_NOT_SCHEDULED      = "Post {} is not in SCHEDULED state (current: {}), skipping.";
    public static final String PUBLISHED_SUCCESS       = "Successfully published post {}";
    // Error Logs
    public static final String ERR_SCHEDULING_QUARTZ   = "Failed to schedule quartz job for post {}";
    public static final String ERR_RESCHEDULING_QUARTZ = "Failed to reschedule quartz job for post {}";
    public static final String ERR_PUBLISH_FAILED      = "Failed to publish post {}";
}
