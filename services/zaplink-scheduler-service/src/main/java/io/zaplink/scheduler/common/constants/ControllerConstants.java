package io.zaplink.scheduler.common.constants;

/**
 * Controller-related constants for Swagger documentation.
 * Contains all controller-specific constants used throughout the Scheduler Service REST API layer.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-02-06
 */
public final class ControllerConstants
{
    private ControllerConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String TAG_SCHEDULER                  = "Scheduler Management";
    public static final String TAG_SCHEDULER_DESC             = "APIs for scheduling, retrieving, and managing social media posts";
    // ==================== SCHEDULER OPERATIONS ====================
    public static final String SCHEDULER_CREATE_SUMMARY       = "Schedule a post";
    public static final String SCHEDULER_CREATE_DESC          = "Schedules a new post for publication at a specified time";
    public static final String SCHEDULER_GET_POSTS_SUMMARY    = "Get scheduled posts";
    public static final String SCHEDULER_GET_POSTS_DESC       = "Retrieves scheduled posts for a user within a time range";
    public static final String SCHEDULER_UPDATE_TIME_SUMMARY  = "Update scheduled time";
    public static final String SCHEDULER_UPDATE_TIME_DESC     = "Updates the scheduled time of an existing post (e.g., via drag-and-drop UI)";
    // ==================== SWAGGER RESPONSE CONSTANTS ====================
    // Success Response Descriptions
    public static final String RESPONSE_200_POST_SCHEDULED    = "Post scheduled successfully";
    public static final String RESPONSE_200_POSTS_RETRIEVED   = "Scheduled posts retrieved successfully";
    public static final String RESPONSE_200_TIME_UPDATED      = "Scheduled time updated successfully";
    // Error Response Descriptions
    public static final String RESPONSE_400_INVALID_DATA      = "Invalid input data or scheduled time in the past";
    public static final String RESPONSE_400_PAST_TIME         = "Cannot schedule post in the past";
    public static final String RESPONSE_404_POST_NOT_FOUND    = "Scheduled post not found";
    public static final String RESPONSE_409_ALREADY_PUBLISHED = "Cannot reschedule an already published post";
    // ==================== SWAGGER PARAMETER CONSTANTS ====================
    public static final String PARAM_POST_ID                  = "Post UUID";
    public static final String PARAM_START_TIME               = "Start time of the range (inclusive)";
    public static final String PARAM_END_TIME                 = "End time of the range (exclusive)";
}
