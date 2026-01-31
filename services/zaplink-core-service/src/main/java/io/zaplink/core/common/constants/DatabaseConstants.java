package io.zaplink.core.common.constants;

/**
 * Database-related constants for table names, column names, and other database configurations.
 * Used across the core service for consistency in database operations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public final class DatabaseConstants {
    
    // Prevent instantiation
    private DatabaseConstants() {
        throw new UnsupportedOperationException("Class cannot be instantiated");
    }
    
    // ==================== TABLE NAMES ====================
    
    // Organization and Team Management
    public static final String TABLE_ORGANIZATIONS = "zaplink_core.organizations";
    public static final String TABLE_TEAMS = "zaplink_core.teams";
    public static final String TABLE_TEAM_MEMBERS = "zaplink_core.team_members";
    public static final String TABLE_CAMPAIGNS = "zaplink_core.campaigns";
    public static final String TABLE_CAMPAIGN_ASSIGNMENTS = "zaplink_core.campaign_assignments";
    public static final String TABLE_POSTS = "zaplink_core.posts";
    
    // ==================== COLUMN NAMES ====================
    
    // Common columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_IS_ACTIVE = "is_active";
    
    // Organization columns
    public static final String COLUMN_OWNER_ID = "owner_id";
    
    // Team columns
    public static final String COLUMN_ORGANIZATION_ID = "organization_id";
    public static final String COLUMN_TEAM_ID = "team_id";
    
    // Team Member columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_INVITED_BY = "invited_by";
    public static final String COLUMN_INVITED_AT = "invited_at";
    public static final String COLUMN_JOINED_AT = "joined_at";
    
    // Campaign columns
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_CREATED_BY = "created_by";
    
    // Campaign Assignment columns
    public static final String COLUMN_CAMPAIGN_ID = "campaign_id";
    public static final String COLUMN_TEAM_MEMBER_ID = "team_member_id";
    public static final String COLUMN_ASSIGNED_AT = "assigned_at";
    public static final String COLUMN_COMPLETED_AT = "completed_at";
    
    // Post columns
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_AUTHOR_ID = "author_id";
    public static final String COLUMN_SUBMITTED_AT = "submitted_at";
    public static final String COLUMN_REVIEWED_BY = "reviewed_by";
    public static final String COLUMN_REVIEWED_AT = "reviewed_at";
    public static final String COLUMN_REVIEW_COMMENTS = "review_comments";
    public static final String COLUMN_PUBLISHED_AT = "published_at";
    
    // ==================== INDEX NAMES ====================
    
    public static final String INDEX_TEAM_MEMBERS_TEAM_ID = "idx_team_members_team_id";
    public static final String INDEX_TEAM_MEMBERS_USER_ID = "idx_team_members_user_id";
    public static final String INDEX_TEAM_MEMBERS_ROLE = "idx_team_members_role";
    public static final String INDEX_CAMPAIGNS_ORGANIZATION_ID = "idx_campaigns_organization_id";
    public static final String INDEX_CAMPAIGNS_STATUS = "idx_campaigns_status";
    public static final String INDEX_POSTS_AUTHOR_ID = "idx_posts_author_id";
    public static final String INDEX_POSTS_STATUS = "idx_posts_status";
    public static final String INDEX_POSTS_CAMPAIGN_ID = "idx_posts_campaign_id";
}
