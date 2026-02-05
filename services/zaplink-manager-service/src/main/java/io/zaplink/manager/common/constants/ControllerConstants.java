package io.zaplink.manager.common.constants;

/**
 * Controller-related constants for API paths, Swagger documentation, and endpoint mappings.
 * Contains all controller-specific constants used throughout the REST API layer.
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
    // ==================== API PATH CONSTANTS ====================
    // Bio Controller Paths
    public static final String BIO_BASE_PATH                       = "/bio";
    public static final String BIO_PAGES_PATH                      = "/pages";
    public static final String BIO_LINKS_PATH                      = "/links";
    // QR Controller Paths
    public static final String QR_BASE_PATH                        = "/qr";
    public static final String QR_ANALYTICS_PATH                   = "/analytics";
    public static final String QR_IMAGE_PATH                       = "/image";
    // Short URL Controller Paths
    public static final String SHORT_BASE_PATH                     = "/short";
    public static final String SHORT_LINKS_PATH                    = "/links";
    public static final String SHORT_STATS_PATH                    = "/stats";
    // Team Management Controller Paths
    public static final String TEAM_BASE_PATH                      = "/teams";
    public static final String TEAM_MEMBERS_PATH                   = "/members";
    public static final String TEAM_INFLUENCERS_PATH               = "/influencers";
    public static final String TEAM_STATISTICS_PATH                = "/statistics";
    // ==================== HEADER CONSTANTS ====================
    public static final String HEADER_USER_EMAIL                   = "X-User-Email";
    public static final String HEADER_ORG_ID                       = "X-Org-Id";
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String TAG_BIO_READ                        = "Bio Pages & Links (Read)";
    public static final String TAG_BIO_READ_DESC                   = "Read-optimized APIs for bio pages and bio links";
    public static final String TAG_QR_MANAGEMENT                   = "QR Code Management";
    public static final String TAG_QR_MANAGEMENT_DESC              = "APIs for managing dynamic QR codes and analytics";
    public static final String TAG_SHORT_URL                       = "Short URL Management";
    public static final String TAG_SHORT_URL_DESC                  = "APIs for short URL management, stats, and analytics";
    public static final String TAG_TEAM_MANAGEMENT                 = "Team Management";
    public static final String TAG_TEAM_MANAGEMENT_DESC            = "Read-optimized APIs for team member information";
    // ==================== BIO CONTROLLER OPERATIONS ====================
    public static final String BIO_GET_PAGE_SUMMARY                = "Get bio page by username";
    public static final String BIO_GET_PAGE_DESC                   = "Retrieves a bio page by its unique username";
    public static final String BIO_GET_PAGES_BY_OWNER_SUMMARY      = "Get bio pages by owner";
    public static final String BIO_GET_PAGES_BY_OWNER_DESC         = "Retrieves all bio pages owned by a specific user";
    public static final String BIO_GET_LINK_SUMMARY                = "Get bio link by ID";
    public static final String BIO_GET_LINK_DESC                   = "Retrieves a specific bio link by its ID";
    public static final String BIO_GET_LINKS_BY_PAGE_SUMMARY       = "Get bio links by page";
    public static final String BIO_GET_LINKS_BY_PAGE_DESC          = "Retrieves all bio links for a specific bio page";
    public static final String BIO_GET_ACTIVE_LINKS_SUMMARY        = "Get active bio links";
    public static final String BIO_GET_ACTIVE_LINKS_DESC           = "Retrieves all active bio links for a specific bio page";
    // ==================== QR CONTROLLER OPERATIONS ====================
    public static final String QR_TEST_SUMMARY                     = "Test QR controller";
    public static final String QR_TEST_DESC                        = "Health check endpoint for QR controller";
    public static final String QR_TEST_DB_SUMMARY                  = "Test database connection";
    public static final String QR_TEST_DB_DESC                     = "Tests database connectivity and returns QR code count";
    public static final String QR_GET_SUMMARY                      = "Get dynamic QR by key";
    public static final String QR_GET_DESC                         = "Retrieves a dynamic QR code by its unique key";
    public static final String QR_GET_BY_USER_SUMMARY              = "Get user's QR codes";
    public static final String QR_GET_BY_USER_DESC                 = "Retrieves paginated list of dynamic QR codes for the authenticated user";
    public static final String QR_CREATE_SUMMARY                   = "Create dynamic QR";
    public static final String QR_CREATE_DESC                      = "Creates a new dynamic QR code for the authenticated user";
    public static final String QR_GET_ANALYTICS_SUMMARY            = "Get QR analytics";
    public static final String QR_GET_ANALYTICS_DESC               = "Retrieves analytics data for a specific QR code within optional date range";
    public static final String QR_GENERATE_IMAGE_SUMMARY           = "Generate QR image";
    public static final String QR_GENERATE_IMAGE_DESC              = "Generates and returns the QR code image in PNG format";
    // ==================== SHORT URL CONTROLLER OPERATIONS ====================
    public static final String SHORT_GET_LINKS_SUMMARY             = "Get user's short links";
    public static final String SHORT_GET_LINKS_DESC                = "Retrieves all short links created by the authenticated user";
    public static final String SHORT_GET_STATS_SUMMARY             = "Get user stats";
    public static final String SHORT_GET_STATS_DESC                = "Retrieves statistics for the authenticated user's short links";
    public static final String SHORT_DELETE_LINK_SUMMARY           = "Delete short link";
    public static final String SHORT_DELETE_LINK_DESC              = "Deletes a specific short link by its ID";
    public static final String SHORT_GET_ANALYTICS_SUMMARY         = "Get link analytics";
    public static final String SHORT_GET_ANALYTICS_DESC            = "Retrieves detailed analytics for a specific short link";
    // ==================== TEAM CONTROLLER OPERATIONS ====================
    public static final String TEAM_GET_MEMBERS_SUMMARY            = "Get team members";
    public static final String TEAM_GET_MEMBERS_DESC               = "Retrieves all team members for a specific team";
    public static final String TEAM_GET_ORG_MEMBERS_SUMMARY        = "Get organization members";
    public static final String TEAM_GET_ORG_MEMBERS_DESC           = "Retrieves all team members for an organization";
    public static final String TEAM_GET_BY_ROLE_SUMMARY            = "Get members by role";
    public static final String TEAM_GET_BY_ROLE_DESC               = "Retrieves team members filtered by their role";
    public static final String TEAM_GET_INFLUENCERS_SUMMARY        = "Get influencers";
    public static final String TEAM_GET_INFLUENCERS_DESC           = "Retrieves all influencers in an organization";
    public static final String TEAM_GET_STATISTICS_SUMMARY         = "Get team statistics";
    public static final String TEAM_GET_STATISTICS_DESC            = "Retrieves team member statistics for an organization";
    // ==================== RESPONSE DESCRIPTIONS ====================
    // Success Responses
    public static final String RESPONSE_200_BIO_PAGE_RETRIEVED     = "Bio page retrieved successfully";
    public static final String RESPONSE_200_BIO_PAGES_RETRIEVED    = "Bio pages retrieved successfully";
    public static final String RESPONSE_200_BIO_LINK_RETRIEVED     = "Bio link retrieved successfully";
    public static final String RESPONSE_200_BIO_LINKS_RETRIEVED    = "Bio links retrieved successfully";
    public static final String RESPONSE_200_QR_RETRIEVED           = "QR code retrieved successfully";
    public static final String RESPONSE_200_QRS_RETRIEVED          = "QR codes retrieved successfully";
    public static final String RESPONSE_200_QR_CREATED             = "QR code created successfully";
    public static final String RESPONSE_200_QR_ANALYTICS_RETRIEVED = "QR analytics retrieved successfully";
    public static final String RESPONSE_200_QR_IMAGE_GENERATED     = "QR image generated successfully";
    public static final String RESPONSE_200_LINKS_RETRIEVED        = "Short links retrieved successfully";
    public static final String RESPONSE_200_STATS_RETRIEVED        = "Stats retrieved successfully";
    public static final String RESPONSE_200_LINK_DELETED           = "Link deleted successfully";
    public static final String RESPONSE_200_LINK_ANALYTICS         = "Link analytics retrieved successfully";
    public static final String RESPONSE_200_MEMBERS_RETRIEVED      = "Team members retrieved successfully";
    public static final String RESPONSE_200_INFLUENCERS_RETRIEVED  = "Influencers retrieved successfully";
    public static final String RESPONSE_200_STATISTICS_RETRIEVED   = "Statistics retrieved successfully";
    public static final String RESPONSE_200_TEST_SUCCESS           = "Test endpoint working";
    public static final String RESPONSE_200_DB_TEST_SUCCESS        = "Database connection successful";
    // Error Responses
    public static final String RESPONSE_400_INVALID_REQUEST        = "Invalid request parameters";
    public static final String RESPONSE_400_INVALID_DATE_FORMAT    = "Invalid date format provided";
    public static final String RESPONSE_404_BIO_PAGE_NOT_FOUND     = "Bio page not found";
    public static final String RESPONSE_404_BIO_LINK_NOT_FOUND     = "Bio link not found";
    public static final String RESPONSE_404_QR_NOT_FOUND           = "QR code not found";
    public static final String RESPONSE_404_LINK_NOT_FOUND         = "Short link not found";
    public static final String RESPONSE_404_TEAM_NOT_FOUND         = "Team not found";
    public static final String RESPONSE_500_DB_ERROR               = "Database connection error";
    public static final String RESPONSE_500_QR_GENERATION_ERROR    = "Failed to generate QR image";
    // ==================== PARAMETER DESCRIPTIONS ====================
    public static final String PARAM_USERNAME                      = "Username of the bio page";
    public static final String PARAM_OWNER_ID                      = "Owner ID of the bio pages";
    public static final String PARAM_BIO_LINK_ID                   = "Bio link ID";
    public static final String PARAM_BIO_PAGE_ID                   = "Bio page ID";
    public static final String PARAM_QR_KEY                        = "Unique QR code key";
    public static final String PARAM_PAGE                          = "Page number (0-indexed)";
    public static final String PARAM_SIZE                          = "Page size";
    public static final String PARAM_START_DATE                    = "Start date for analytics (ISO format)";
    public static final String PARAM_END_DATE                      = "End date for analytics (ISO format)";
    public static final String PARAM_LINK_ID                       = "Short link ID";
    public static final String PARAM_LINK_KEY                      = "Short link key";
    public static final String PARAM_TEAM_ID                       = "Team ID";
    public static final String PARAM_ORG_ID                        = "Organization ID";
    public static final String PARAM_ROLE                          = "Team member role";
}
