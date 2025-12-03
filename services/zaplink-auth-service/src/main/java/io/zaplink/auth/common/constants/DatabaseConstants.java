package io.zaplink.auth.common.constants;

/**
 * Database-related constants for table names, column names, and schema definitions.
 * Contains all database entity and JPA-related constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
public final class DatabaseConstants
{
    // Prevent instantiation
    private DatabaseConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== TABLE NAMES ====================
    public static final String TABLE_USERS                       = "users";
    public static final String TABLE_ROLES                       = "roles";
    public static final String TABLE_REFRESH_TOKENS              = "refresh_tokens";
    public static final String TABLE_USER_ROLES                  = "user_roles";
    public static final String TABLE_LOGIN_ATTEMPTS              = "login_attempts";
    // ==================== COLUMN NAMES ====================
    public static final String COLUMN_USER_ID                    = "user_id";
    public static final String COLUMN_ROLE_ID                    = "role_id";
    public static final String COLUMN_EMAIL                      = "email";
    public static final String COLUMN_USERNAME                   = "username";
    public static final String COLUMN_PASSWORD                   = "password";
    public static final String COLUMN_FIRST_NAME                 = "first_name";
    public static final String COLUMN_LAST_NAME                  = "last_name";
    public static final String COLUMN_PHONE_NUMBER               = "phone_number";
    public static final String COLUMN_IS_ACTIVE                  = "is_active";
    public static final String COLUMN_IS_VERIFIED                = "is_verified";
    public static final String COLUMN_VERIFICATION_TOKEN         = "verification_token";
    public static final String COLUMN_RESET_TOKEN                = "reset_token";
    public static final String COLUMN_RESET_TOKEN_EXPIRY         = "reset_token_expiry";
    public static final String COLUMN_CREATED_AT                 = "created_at";
    public static final String COLUMN_UPDATED_AT                 = "updated_at";
    public static final String COLUMN_EXPIRY_DATE                = "expiry_date";
    public static final String COLUMN_IP_ADDRESS                 = "ip_address";
    public static final String COLUMN_DESCRIPTION                = "description";
    public static final String COLUMN_USER_AGENT                 = "user_agent";
    public static final String COLUMN_FAILURE_REASON             = "failure_reason";
    public static final String COLUMN_LAST_ATTEMPT_AT            = "last_attempt_at";
    public static final String COLUMN_LOCKED_UNTIL               = "locked_until";
    // ==================== JPQL QUERIES ====================
    public static final String QUERY_UPDATE_RESET_TOKEN          = "UPDATE User u SET u.resetToken = :token, u.resetTokenExpiry = :expiry WHERE u.email = :email";
    public static final String QUERY_CLEAR_RESET_TOKEN           = "UPDATE User u SET u.resetToken = null, u.resetTokenExpiry = null WHERE u.email = :email";
    public static final String QUERY_VERIFY_USER                 = "UPDATE User u SET u.verified = true, u.verificationToken = null WHERE u.id = :userId";
    public static final String QUERY_COUNT_FAILED_ATTEMPTS_SINCE = "SELECT COUNT(la) FROM LoginAttempt la WHERE la.email = :email AND la.successful = false AND la.createdAt >= :since";
    public static final String QUERY_RECENT_FAILED_ATTEMPTS      = "SELECT la FROM LoginAttempt la WHERE la.successful = false AND la.createdAt >= :since";
    public static final String QUERY_DELETE_EXPIRED_TOKENS       = "DELETE FROM RefreshToken rt WHERE rt.expiryDate < :date";
    public static final String QUERY_DELETE_BY_USER              = "DELETE FROM RefreshToken rt WHERE rt.user = :user";
    // ==================== PARAM NAME ====================
    public static final String PARAM_EMAIL                       = "email";
    public static final String PARAM_USER_ID                     = "user_id";
    public static final String PARAM_USER                        = "user";
    public static final String PARAM_EXPIRY                      = "expiry";
    public static final String PARAM_TOKEN                       = "token";
    public static final String PARAM_DATE                        = "date";
    public static final String PARAM_SINCE                       = "since";
}
