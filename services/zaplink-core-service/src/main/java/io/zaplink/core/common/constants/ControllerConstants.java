package io.zaplink.core.common.constants;

/**
 * Controller-related constants for API paths, endpoint mappings, and default values.
 * Contains all controller-specific constants used throughout the REST API layer.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
*/
public final class ControllerConstants
{
    // Prevent instantiation
    private ControllerConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== API PATH CONSTANTS ====================
    // API Version
    public static final String  API_VERSION_1                        = "1";
    // Bio Controller Paths
    public static final String  BIO_BASE_PATH                        = "/bio";
    public static final String  BIO_PAGE_PATH                        = "/page";
    public static final String  BIO_LINK_PATH                        = "/link";
    public static final String  BIO_LINK_REORDER_PATH                = "/link/{pageId}/reorder";
    // QR Controller Paths
    public static final String  QR_BASE_PATH                         = "/qr";
    public static final String  QR_DYNAMIC_PATH                      = "/dyqr";
    public static final String  QR_STYLED_PATH                       = "/styled";
    public static final String  QR_OPTIONS_PATH                      = "/options";
    public static final String  QR_DESTINATION_PATH                  = "/dyqr/{qrKey}/destination";
    public static final String  QR_STATUS_PATH                       = "/dyqr/{qrKey}/status";
    // Core Controller Paths
    public static final String  SHORT_BASE_PATH                      = "/short";
    public static final String  SHORT_URL_PATH                       = "/url";
    // Team Management Controller Paths
    public static final String  TEAM_BASE_PATH                       = "/teams";
    public static final String  TEAM_INVITE_PATH                     = "/invite";
    public static final String  TEAM_MEMBER_ROLE_PATH                = "/members/{userId}/role";
    public static final String  TEAM_MEMBER_REMOVE_PATH              = "/members/{userId}";
    public static final String  TEAM_MEMBERS_PATH                    = "/{teamId}/members";
    // Workflow Controller Paths
    public static final String  WORKFLOW_BASE_PATH                   = "/workflow";
    public static final String  WORKFLOW_SUBMIT_PATH                 = "/submit";
    public static final String  WORKFLOW_APPROVE_REJECT_PATH         = "/approve-reject";
    public static final String  WORKFLOW_PENDING_PATH                = "/pending";
    public static final String  WORKFLOW_POSTS_AUTHOR_PATH           = "/posts/author/{authorId}";
    // QR Debug Controller Paths
    public static final String  QR_DEBUG_BASE_PATH                   = "/qr/debug";
    public static final String  QR_DEBUG_SIMPLE_PATH                 = "/simple";
    public static final String  QR_DEBUG_STYLED_PATH                 = "/styled";
    public static final String  QR_DEBUG_TEST_CONFIG_PATH            = "/test-config";
    public static final String  QR_DEBUG_VERIFY_PATH                 = "/verify";
    public static final String  QR_DEBUG_VERIFY_NATIVE_PATH          = "/verify-native";
    public static final String  QR_DEBUG_VERIFY_STYLED_PATH          = "/verify-styled";
    // ==================== DEFAULT VALUE CONSTANTS ====================
    // QR Debug Default Values
    public static final String  DEFAULT_QR_DATA                      = "https://zaplink.io";
    public static final String  DEFAULT_TEST_DATA                    = "TEST";
    public static final int     DEFAULT_QR_SIZE                      = 512;
    public static final String  DEFAULT_BODY_SHAPE                   = "ROUNDED";
    public static final String  DEFAULT_EYE_SHAPE                    = "SQUARE";
    public static final String  DEFAULT_COLOR                        = "#0066FF";
    public static final String  DEFAULT_BACKGROUND_COLOR             = "#003D99";
    public static final String  DEFAULT_FOREGROUND_COLOR             = "#000000";
    public static final String  DEFAULT_WHITE_COLOR                  = "#FFFFFF";
    // QR Configuration Defaults
    public static final int     DEFAULT_MARGIN                       = 1;
    public static final int     DEFAULT_STYLED_MARGIN                = 2;
    public static final String  DEFAULT_ERROR_CORRECTION             = "H";
    public static final boolean DEFAULT_TRANSPARENT                  = false;
    // ==================== PARAMETER NAME CONSTANTS ====================
    // Request Parameter Names
    public static final String  PARAM_DATA                           = "data";
    public static final String  PARAM_SIZE                           = "size";
    public static final String  PARAM_BODY_SHAPE                     = "bodyShape";
    public static final String  PARAM_EYE_SHAPE                      = "eyeShape";
    public static final String  PARAM_COLOR                          = "color";
    public static final String  PARAM_TEAM_ID                        = "teamId";
    public static final String  PARAM_USER_ID                        = "userId";
    public static final String  PARAM_QR_KEY                         = "qrKey";
    // Header Names
    public static final String  HEADER_USER_EMAIL                    = "X-User-Email";
    public static final String  HEADER_USER_ID                       = "X-User-Id";
    public static final String  HEADER_ORG_ID                        = "X-Org-Id";
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String  TAG_BIO_MANAGEMENT                   = "Bio Link Management";
    public static final String  TAG_QR_MANAGEMENT                    = "QR Management";
    public static final String  TAG_URL_SHORTENER                    = "URL Shortener";
    public static final String  TAG_TEAM_MANAGEMENT                  = "Team Management";
    public static final String  TAG_WORKFLOW_MANAGEMENT              = "Workflow Management";
    // Tag Descriptions
    public static final String  TAG_BIO_MANAGEMENT_DESC              = "APIs for managing bio links";
    public static final String  TAG_QR_MANAGEMENT_DESC               = "APIs for managing QR codes";
    public static final String  TAG_URL_SHORTENER_DESC               = "APIs for URL shortening and management";
    public static final String  TAG_TEAM_MANAGEMENT_DESC             = "APIs for managing team members and roles";
    public static final String  TAG_WORKFLOW_MANAGEMENT_DESC         = "APIs for managing post approval workflows";
    // ==================== MEDIA TYPE CONSTANTS ====================
    public static final String  MEDIA_IMAGE_PNG                      = "image/png";
    // ==================== SWAGGER OPERATION CONSTANTS ====================
    // Bio Controller Operations
    public static final String  BIO_CREATE_PAGE_SUMMARY              = "Create bio page";
    public static final String  BIO_CREATE_PAGE_DESC                 = "Creates a new bio page with the specified details";
    public static final String  BIO_UPDATE_PAGE_SUMMARY              = "Update bio page";
    public static final String  BIO_UPDATE_PAGE_DESC                 = "Updates an existing bio page with new details";
    public static final String  BIO_DELETE_PAGE_SUMMARY              = "Delete bio page";
    public static final String  BIO_DELETE_PAGE_DESC                 = "Deletes a bio page and all its associated links";
    public static final String  BIO_CREATE_LINK_SUMMARY              = "Create bio link";
    public static final String  BIO_CREATE_LINK_DESC                 = "Creates a new bio link within a bio page";
    public static final String  BIO_UPDATE_LINK_SUMMARY              = "Update bio link";
    public static final String  BIO_UPDATE_LINK_DESC                 = "Updates an existing bio link with new details";
    public static final String  BIO_DELETE_LINK_SUMMARY              = "Delete bio link";
    public static final String  BIO_DELETE_LINK_DESC                 = "Deletes a bio link from its bio page";
    public static final String  BIO_REORDER_LINKS_SUMMARY            = "Reorder bio links";
    public static final String  BIO_REORDER_LINKS_DESC               = "Reorders the links within a bio page based on the specified order";
    // QR Controller Operations
    public static final String  QR_CREATE_DYNAMIC_SUMMARY            = "Create dynamic QR";
    public static final String  QR_CREATE_DYNAMIC_DESC               = "Creates a new dynamic QR code with configurable destination and tracking";
    public static final String  QR_UPDATE_DYNAMIC_SUMMARY            = "Update dynamic QR";
    public static final String  QR_UPDATE_DYNAMIC_DESC               = "Updates an existing dynamic QR code configuration";
    public static final String  QR_UPDATE_DESTINATION_SUMMARY        = "Update QR destination";
    public static final String  QR_UPDATE_DESTINATION_DESC           = "Updates the destination URL of a dynamic QR code";
    public static final String  QR_TOGGLE_STATUS_SUMMARY             = "Toggle QR status";
    public static final String  QR_TOGGLE_STATUS_DESC                = "Activates or deactivates a dynamic QR code";
    public static final String  QR_DELETE_DYNAMIC_SUMMARY            = "Delete dynamic QR";
    public static final String  QR_DELETE_DYNAMIC_DESC               = "Permanently deletes a dynamic QR code";
    public static final String  QR_GENERATE_STYLED_SUMMARY           = "Generate styled QR";
    public static final String  QR_GENERATE_STYLED_DESC              = "Generates a styled QR code image with custom colors, shapes, and effects";
    public static final String  QR_GET_OPTIONS_SUMMARY               = "Get QR options";
    public static final String  QR_GET_OPTIONS_DESC                  = "Retrieves available QR customization options including shapes, colors, and gradients";
    // Core Controller Operations
    public static final String  CORE_CREATE_SHORT_URL_SUMMARY        = "Create short URL";
    public static final String  CORE_CREATE_SHORT_URL_DESC           = "Creates a short URL from a long URL with optional metadata";
    public static final String  CORE_UPDATE_SHORT_URL_SUMMARY        = "Update short URL";
    public static final String  CORE_UPDATE_SHORT_URL_DESC           = "Updates an existing short URL with new destination or metadata";
    // Team Management Controller Operations
    public static final String  TEAM_INVITE_MEMBER_SUMMARY           = "Invite team member";
    public static final String  TEAM_INVITE_MEMBER_DESC              = "Invites a user to join a team with a specific role";
    public static final String  TEAM_CHANGE_ROLE_SUMMARY             = "Change team member role";
    public static final String  TEAM_CHANGE_ROLE_DESC                = "Changes the role of an existing team member";
    public static final String  TEAM_REMOVE_MEMBER_SUMMARY           = "Remove team member";
    public static final String  TEAM_REMOVE_MEMBER_DESC              = "Removes a team member from a team";
    public static final String  TEAM_GET_MEMBERS_SUMMARY             = "Get team members";
    public static final String  TEAM_GET_MEMBERS_DESC                = "Retrieves all team members for a specific team";
    // Workflow Controller Operations
    public static final String  WORKFLOW_SUBMIT_POST_SUMMARY         = "Submit post for approval";
    public static final String  WORKFLOW_SUBMIT_POST_DESC            = "Submits a post for approval in the workflow";
    public static final String  WORKFLOW_REVIEW_POST_SUMMARY         = "Approve or reject post";
    public static final String  WORKFLOW_REVIEW_POST_DESC            = "Approves or rejects a submitted post";
    public static final String  WORKFLOW_GET_PENDING_SUMMARY         = "Get pending posts";
    public static final String  WORKFLOW_GET_PENDING_DESC            = "Retrieves all posts pending approval";
    public static final String  WORKFLOW_GET_POSTS_BY_AUTHOR_SUMMARY = "Get posts by author";
    public static final String  WORKFLOW_GET_POSTS_BY_AUTHOR_DESC    = "Retrieves all posts created by a specific author";
    // ==================== SWAGGER RESPONSE CONSTANTS ====================
    // Success Response Descriptions
    public static final String  RESPONSE_200_BIO_PAGE_CREATED        = "Bio page created successfully";
    public static final String  RESPONSE_200_BIO_PAGE_UPDATED        = "Bio page updated successfully";
    public static final String  RESPONSE_200_BIO_PAGE_DELETED        = "Bio page deleted successfully";
    public static final String  RESPONSE_200_BIO_LINK_CREATED        = "Bio link created successfully";
    public static final String  RESPONSE_200_BIO_LINK_UPDATED        = "Bio link updated successfully";
    public static final String  RESPONSE_200_BIO_LINK_DELETED        = "Bio link deleted successfully";
    public static final String  RESPONSE_200_LINKS_REORDERED         = "Links reordered successfully";
    public static final String  RESPONSE_200_DYNAMIC_QR_CREATED      = "Dynamic QR created successfully";
    public static final String  RESPONSE_200_DYNAMIC_QR_UPDATED      = "Dynamic QR updated successfully";
    public static final String  RESPONSE_200_DESTINATION_UPDATED     = "Destination updated successfully";
    public static final String  RESPONSE_200_QR_STATUS_TOGGLED       = "QR status toggled successfully";
    public static final String  RESPONSE_200_DYNAMIC_QR_DELETED      = "Dynamic QR deleted successfully";
    public static final String  RESPONSE_200_QR_IMAGE_GENERATED      = "QR code image generated successfully";
    public static final String  RESPONSE_200_QR_OPTIONS_RETRIEVED    = "QR options retrieved successfully";
    public static final String  RESPONSE_200_SHORT_URL_CREATED       = "Short URL created successfully";
    public static final String  RESPONSE_200_SHORT_URL_UPDATED       = "Short URL updated successfully";
    public static final String  RESPONSE_200_TEAM_MEMBER_INVITED     = "Team member invited successfully";
    public static final String  RESPONSE_200_TEAM_ROLE_CHANGED       = "Team member role changed successfully";
    public static final String  RESPONSE_200_TEAM_MEMBERS_RETRIEVED  = "Team members retrieved successfully";
    public static final String  RESPONSE_200_POST_SUBMITTED          = "Post submitted successfully";
    public static final String  RESPONSE_200_POST_REVIEWED           = "Post reviewed successfully";
    public static final String  RESPONSE_200_PENDING_POSTS_RETRIEVED = "Pending posts retrieved successfully";
    public static final String  RESPONSE_200_AUTHOR_POSTS_RETRIEVED  = "Author posts retrieved successfully";
    // Created Response Descriptions
    public static final String  RESPONSE_201_POST_SUBMITTED          = "Post submitted successfully";
    public static final String  RESPONSE_201_TEAM_MEMBER_INVITED     = "Team member invited successfully";
    // No Content Response Descriptions
    public static final String  RESPONSE_204_TEAM_MEMBER_REMOVED     = "Team member removed successfully";
    // Error Response Descriptions
    public static final String  RESPONSE_400_INVALID_DATA            = "Invalid input data";
    public static final String  RESPONSE_400_INVALID_POST_DATA       = "Invalid post data";
    public static final String  RESPONSE_400_INVALID_REVIEW_DATA     = "Invalid review data";
    public static final String  RESPONSE_400_INVALID_INVITATION_DATA = "Invalid invitation data";
    public static final String  RESPONSE_400_INVALID_ROLE_DATA       = "Invalid role change data";
    public static final String  RESPONSE_400_INVALID_TEAM_DATA       = "Invalid team member data";
    public static final String  RESPONSE_400_INVALID_URL_DATA        = "Invalid URL or request data";
    public static final String  RESPONSE_400_INVALID_QR_DATA         = "Invalid QR configuration";
    public static final String  RESPONSE_400_INVALID_DESTINATION     = "Invalid destination URL";
    public static final String  RESPONSE_400_INVALID_REORDER_DATA    = "Invalid reorder data";
    public static final String  RESPONSE_400_QR_GENERATION_FAILED    = "Failed to generate QR code";
    // Forbidden Response Descriptions
    public static final String  RESPONSE_403_AUTHOR_NOT_ACTIVE       = "Author not an active team member";
    public static final String  RESPONSE_403_REVIEWER_NOT_APPROVER   = "Reviewer not an active approver";
    public static final String  RESPONSE_403_INVALID_ORG_ACCESS      = "Invalid organization access";
    // Not Found Response Descriptions
    public static final String  RESPONSE_404_BIO_PAGE_NOT_FOUND      = "Bio page not found";
    public static final String  RESPONSE_404_BIO_LINK_NOT_FOUND      = "Bio link not found";
    public static final String  RESPONSE_404_DYNAMIC_QR_NOT_FOUND    = "Dynamic QR not found or access denied";
    public static final String  RESPONSE_404_SHORT_URL_NOT_FOUND     = "Short URL not found";
    public static final String  RESPONSE_404_TEAM_NOT_FOUND          = "Team not found";
    public static final String  RESPONSE_404_TEAM_MEMBER_NOT_FOUND   = "Team member not found";
    public static final String  RESPONSE_404_POST_NOT_FOUND          = "Post not found";
    public static final String  RESPONSE_404_AUTHOR_NOT_FOUND        = "Author not found";
    public static final String  RESPONSE_404_ORGANIZATION_NOT_FOUND  = "Organization not found";
    // Conflict Response Descriptions
    public static final String  RESPONSE_409_USERNAME_EXISTS         = "Username already exists";
    public static final String  RESPONSE_409_QR_NAME_EXISTS          = "QR name already exists for user";
    public static final String  RESPONSE_409_SHORT_URL_EXISTS        = "Short URL already exists";
    public static final String  RESPONSE_409_USER_ALREADY_MEMBER     = "User already a member of the team";
    public static final String  RESPONSE_409_POST_NOT_SUBMITTED      = "Post not in submitted status";
    // Parameter Descriptions
    public static final String  PARAM_DESC_BIO_PAGE_ID               = "Bio page ID";
    public static final String  PARAM_DESC_BIO_LINK_ID               = "Bio link ID";
    public static final String  PARAM_DESC_QR_KEY                    = "Dynamic QR key";
    public static final String  PARAM_DESC_USER_ID                   = "User ID of the team member";
    public static final String  PARAM_DESC_USER_ID_REMOVE            = "User ID of the team member to remove";
    public static final String  PARAM_DESC_TEAM_ID_REMOVE            = "Team ID to remove from";
    public static final String  PARAM_DESC_TEAM_ID                   = "Team ID";
    public static final String  PARAM_DESC_ORG_ID                    = "Organization ID";
    public static final String  PARAM_DESC_AUTHOR_ID                 = "Author ID";
}
