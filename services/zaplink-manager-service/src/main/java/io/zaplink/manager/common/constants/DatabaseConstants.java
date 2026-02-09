package io.zaplink.manager.common.constants;

/**
 * Database-related constants for Manager Service read model tables.
 * Used across the manager service for consistency in database operations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public final class DatabaseConstants
{
    // Prevent instantiation
    private DatabaseConstants()
    {
        throw new UnsupportedOperationException( "Class cannot be instantiated" );
    }
    // ==================== TABLE NAMES ====================
    // Read Model Tables
    public static final String TABLE_TEAM_MEMBER_VIEW         = "team_member_view";
    public static final String TABLE_PENDING_POST_VIEW        = "pending_post_view";
    public static final String TABLE_INFLUENCER_CAMPAIGN_VIEW = "influencer_campaign_view";
    // ==================== COLUMN NAMES ====================
    // Common columns
    public static final String COLUMN_ID                      = "id";
    public static final String COLUMN_NAME                    = "name";
    public static final String COLUMN_DESCRIPTION             = "description";
    public static final String COLUMN_CREATED_AT              = "created_at";
    public static final String COLUMN_UPDATED_AT              = "updated_at";
    public static final String COLUMN_LAST_UPDATED            = "last_updated";
    // Team Member View columns
    public static final String COLUMN_TEAM_ID                 = "team_id";
    public static final String COLUMN_TEAM_NAME               = "team_name";
    public static final String COLUMN_USER_ID                 = "user_id";
    public static final String COLUMN_USERNAME                = "username";
    public static final String COLUMN_USER_EMAIL              = "user_email";
    public static final String COLUMN_FIRST_NAME              = "first_name";
    public static final String COLUMN_LAST_NAME               = "last_name";
    public static final String COLUMN_ROLE                    = "role";
    public static final String COLUMN_STATUS                  = "status";
    public static final String COLUMN_INVITED_AT              = "invited_at";
    public static final String COLUMN_JOINED_AT               = "joined_at";
    public static final String COLUMN_ORGANIZATION_ID         = "organization_id";
    public static final String COLUMN_ORGANIZATION_NAME       = "organization_name";
    // Pending Post View columns
    public static final String COLUMN_TITLE                   = "title";
    public static final String COLUMN_CONTENT                 = "content";
    public static final String COLUMN_AUTHOR_ID               = "author_id";
    public static final String COLUMN_AUTHOR_NAME             = "author_name";
    public static final String COLUMN_AUTHOR_EMAIL            = "author_email";
    public static final String COLUMN_CAMPAIGN_ID             = "campaign_id";
    public static final String COLUMN_CAMPAIGN_NAME           = "campaign_name";
    public static final String COLUMN_SUBMITTED_AT            = "submitted_at";
    // Influencer Campaign View columns
    public static final String COLUMN_CAMPAIGN_STATUS         = "campaign_status";
    public static final String COLUMN_START_DATE              = "start_date";
    public static final String COLUMN_END_DATE                = "end_date";
    public static final String COLUMN_TEAM_MEMBER_ID          = "team_member_id";
    public static final String COLUMN_ASSIGNMENT_STATUS       = "assignment_status";
    public static final String COLUMN_ASSIGNED_AT             = "assigned_at";
    public static final String COLUMN_COMPLETED_AT            = "completed_at";
}
