package io.zaplink.scheduler.common.constants;

/**
 * Database and Job Identifiers.
 * <p>
 * Constants for Quartz Job groups, keys, and data map keys.
 * </p>
 */
public final class DbIdentifiers
{
    private DbIdentifiers()
    {
    }
    public static final String JOB_GROUP_NAME                = "post-publishing-jobs";
    public static final String JOB_DATA_POST_ID              = "postId";
    // Table Names
    public static final String TABLE_SCHEDULED_POSTS         = "scheduled_posts";
    public static final String TABLE_SCHEDULED_POST_MEDIA    = "scheduled_post_media";
    public static final String TABLE_SCHEDULED_POST_ACCOUNTS = "scheduled_post_social_accounts";
    // Column Names
    public static final String COL_MEDIA_ASSET_ID            = "media_asset_id";
    public static final String COL_SCHEDULED_TIME            = "scheduled_time";
    public static final String COL_SOCIAL_ACCOUNT_ID         = "social_account_id";
    public static final String COL_OWNER_ID                  = "owner_id";
    public static final String COL_CREATED_AT                = "created_at";
    public static final String COL_UPDATED_AT                = "updated_at";
    // Join Columns
    public static final String JOIN_COL_SCHEDULED_POST_ID    = "scheduled_post_id";
}
