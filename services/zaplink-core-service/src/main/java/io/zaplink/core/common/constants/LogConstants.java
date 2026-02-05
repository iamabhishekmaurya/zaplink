package io.zaplink.core.common.constants;

/**
 * Logging-related constants for log message templates and logging patterns.
 * Contains all structured logging message templates used throughout the application.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-26
 */
public final class LogConstants
{
    // Prevent instantiation
    private LogConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== TRACE ID LOGS ====================
    public static final String LOG_TRACE_ID_SET_FOR_THREAD           = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD       = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER          = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW            = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE        = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE      = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== EXCEPTION HANDLING LOGS ====================
    public static final String LOG_AUTH_EXCEPTION_OCCURRED           = "AuthException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_ALREADY_EXISTS_EXCEPTION     = "UserAlreadyExistsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_NOT_FOUND_EXCEPTION          = "UserNotFoundException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_INVALID_CREDENTIALS_EXCEPTION     = "InvalidCredentialsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_AUTHENTICATION_EXCEPTION          = "AuthenticationException occurred - TraceId: {}, Error: {}";
    public static final String LOG_BAD_CREDENTIALS_EXCEPTION         = "BadCredentialsException occurred - TraceId: {}, Error: {}";
    public static final String LOG_ACCESS_DENIED_EXCEPTION           = "AccessDeniedException occurred - TraceId: {}, Error: {}";
    public static final String LOG_VALIDATION_EXCEPTION_OCCURRED     = "MethodArgumentNotValidException occurred - TraceId: {}, Validation errors: {}";
    public static final String LOG_UNEXPECTED_EXCEPTION_OCCURRED     = "Unexpected exception occurred - TraceId: {}, Error: {}";
    // ==================== URL SHORTENER LOGS ====================
    public static final String LOG_SHORT_URL_INIT                    = "Initializing short URL creation";
    public static final String LOG_URL_MAPPING_CREATED               = "URL mapping created successfully";
    public static final String LOG_URL_MAPPING_NOT_CREATED           = "URL mapping creation failed";
    public static final String LOG_URL_SHORTENING_EXCEPTION          = "Exception during URL shortening: {}";
    public static final String LOG_CREATING_URL_MAPPING              = "Creating URL mapping with key: {}";
    public static final String LOG_UPDATING_SHORT_URL                = "Updating short URL for key: {}";
    public static final String LOG_URL_MAPPING_UPDATED               = "URL mapping updated successfully for key: {}";
    // ==================== EVENT PUBLISHING LOGS ====================
    public static final String EVENT_PUBLISHING                      = "Publishing event: {}";
    public static final String EVENT_PUBLISHED                       = "Event published successfully: {}";
    public static final String EVENT_PUBLISH_ERROR                   = "Failed to publish event: {}";
    // ==================== TEAM MANAGEMENT LOGS ====================
    public static final String TEAM_MEMBER_INVITING                  = "Inviting team member: {}";
    public static final String TEAM_MEMBER_INVITED                   = "Team member invited successfully: {}";
    public static final String TEAM_MEMBER_ROLE_CHANGING             = "Changing team member role: {}";
    public static final String TEAM_MEMBER_ROLE_CHANGED              = "Team member role changed successfully: {}";
    public static final String TEAM_MEMBER_REMOVING                  = "Removing team member: {}";
    public static final String TEAM_MEMBER_REMOVED                   = "Team member removed successfully: {}";
    public static final String TEAM_MEMBER_INVITING_WITH_USER        = "Inviting team member: {} by user: {}";
    public static final String TEAM_MEMBER_ROLE_CHANGING_WITH_USER   = "Changing role for user: {} to: {}";
    public static final String TEAM_MEMBER_REMOVING_FROM_TEAM        = "Removing team member: {} from team: {}";
    public static final String TEAM_MEMBERS_RETRIEVING_FOR_TEAM      = "Retrieving team members for team: {}";
    // ==================== WORKFLOW LOGS ====================
    public static final String POST_SUBMITTING                       = "Submitting post for approval: {}";
    public static final String POST_SUBMITTED                        = "Post submitted for approval successfully: {}";
    public static final String POST_REVIEWING                        = "Reviewing post: {}";
    public static final String POST_REVIEWED                         = "Post reviewed successfully: {}";
    public static final String POST_REVIEWING_WITH_DECISION          = "Reviewing post: {} with decision: {}";
    public static final String POST_PENDING_RETRIEVING_FOR_ORG       = "Retrieving pending posts for organization: {}";
    public static final String POST_RETRIEVING_FOR_AUTHOR            = "Retrieving posts for author: {}";
    // ==================== CONTROLLER LOGS ====================
    public static final String CONTROLLER_GENERATING_SIMPLE_QR       = "Generating simple QR with data: {}, size: {}";
    public static final String CONTROLLER_CONFIG_CREATED             = "Config created: {}";
    public static final String CONTROLLER_QR_GENERATED_SUCCESS       = "QR generated successfully, image size: {}x{}";
    public static final String CONTROLLER_ERROR_GENERATING_QR        = "Error generating QR";
    public static final String CONTROLLER_GENERATING_STYLED_QR       = "Generating styled QR with data: {}, size: {}, shape: {}, color: {}";
    public static final String CONTROLLER_STYLED_CONFIG_CREATED      = "Styled config created: {}";
    public static final String CONTROLLER_STYLED_QR_GENERATED        = "Styled QR generated successfully";
    public static final String CONTROLLER_ERROR_GENERATING_STYLED_QR = "Error generating styled QR";
    public static final String CONTROLLER_VERIFYING_QR_GENERATION    = "Verifying QR generation and decode for data: {}";
    public static final String CONTROLLER_VERIFYING_NATIVE_QR        = "Verifying QR with NATIVE ZXing renderer for data: {}";
    public static final String CONTROLLER_VERIFYING_STYLED_QR        = "Verifying QR with STYLED renderer for data: {}";
    // ==================== DYNAMIC QR LOGS ====================
    public static final String DYNAMIC_QR_CREATING                   = "Creating dynamic QR code. Name: {}, User: {}";
    public static final String DYNAMIC_QR_CREATED                    = "Created dynamic QR with key: {} for user: {}";
    public static final String DYNAMIC_QR_UPDATED                    = "Updated dynamic QR with key: {} for user: {}";
    public static final String DYNAMIC_QR_UPDATING_DESTINATION       = "Updated destination for QR key: {} by user: {}";
    public static final String DYNAMIC_QR_TOGGLING_STATUS            = "Toggled status for QR key: {} to {} by user: {}";
    public static final String DYNAMIC_QR_DELETING                   = "Deleted dynamic QR with key: {} by user: {}";
    public static final String DYNAMIC_QR_ERROR_CONVERTING_CONFIG    = "Error converting QR config to JSON";
    public static final String DYNAMIC_QR_ERROR_PARSING_JSON         = "Error parsing JSON fields for response";
    // ==================== QR GENERATION LOGS ====================
    public static final String QR_ZXING_MARGIN_HINT_SET              = "ZXing margin hint set to: {}";
    public static final String QR_GENERATED_MATRIX                   = "Generated QR matrix: {}x{} for data: {}";
    public static final String QR_ZXING_ENFORCED_MARGIN              = "ZXing enforced additional margin: {} (requested: {})";
    public static final String QR_CROPPING_MATRIX                    = "Cropping matrix to remove ZXing enforced margin...";
    public static final String QR_CROPPED_MATRIX_SIZE                = "Cropped matrix size: {}x{}";
    public static final String QR_FAILED_TO_GENERATE_ADVANCED        = "Failed to generate advanced QR";
    public static final String QR_CANNOT_CROP_MATRIX                 = "Cannot crop matrix - new size would be {}x{}";
    // ==================== QR VERIFICATION LOGS ====================
    public static final String QR_VERIFICATION_SUCCESS               = "QR decoded successfully! Original: {}, Decoded: {}";
    public static final String QR_NATIVE_VERIFICATION_SUCCESS        = "NATIVE QR decoded! Original: {}, Decoded: {}";
    public static final String QR_STYLED_VERIFICATION_SUCCESS        = "STYLED QR decoded! bodyShape: {}, eyeShape: {}, Decoded: {}";
    public static final String QR_VERIFICATION_FAILED                = "QR verification failed";
    public static final String QR_NATIVE_VERIFICATION_FAILED         = "NATIVE QR verification failed";
    public static final String QR_STYLED_VERIFICATION_FAILED         = "STYLED QR verification failed for body: {}, eye: {}";
    // ==================== SUCCESS LOGS ====================
    public static final String SUCCESS_QR_DECODED                    = "QR decoded successfully! Original: {}, Decoded: {}";
    public static final String SUCCESS_NATIVE_QR_DECODED             = "NATIVE QR decoded! Original: {}, Decoded: {}";
    public static final String SUCCESS_STYLED_QR_DECODED             = "STYLED QR decoded! bodyShape: {}, eyeShape: {}, Decoded: {}";
    // ==================== BIO LINK LOGS ====================
    // Bio Link Creation Logs
    public static final String BIOLINK_CREATING_FOR_PAGE             = "Creating bio link for page ID: {}, title: {}, type: {}";
    public static final String BIOLINK_CREATE_SUCCESS                = "Successfully created bio link with ID: {}, title: {}, type: {}";
    public static final String BIOLINK_CREATE_FAILED                 = "Failed to create bio link for page ID: {}, title: {}";
    public static final String BIOLINK_PAGE_NOT_FOUND_WARNING        = "Attempted to create link for non-existent bio page ID: {}";
    // Bio Link Update Logs
    public static final String BIOLINK_UPDATING_WITH_ID              = "Updating bio link with ID: {}";
    public static final String BIOLINK_UPDATE_SUCCESS                = "Updated bio link with ID: {}";
    // Bio Link Deletion Logs
    public static final String BIOLINK_DELETING_WITH_ID              = "Deleting bio link with ID: {}";
    public static final String BIOLINK_DELETE_SUCCESS                = "Deleted bio link with ID: {}";
    // Bio Link Reordering Logs
    public static final String BIOLINK_REORDERING_FOR_PAGE           = "Reordering links for page ID: {}";
    public static final String BIOLINK_REORDER_SUCCESS               = "Reordered {} links for page ID: {}";
    // Bio Link Validation Logs
    public static final String BIOLINK_CREATE_VALIDATION_PASSED      = "Create bio link request validation passed";
    public static final String BIOLINK_CONVERTING_TO_DTO             = "Converting BioLinkEntity to DTO for ID: {}";
    public static final String BIOLINK_CONVERT_TO_DTO_FAILED         = "Failed to convert BioLinkEntity to DTO for ID: {}";
    // Bio Link Event Names
    public static final String BIOLINK_EVENT_CREATED                 = "bio.link-created";
    public static final String BIOLINK_EVENT_UPDATED                 = "bio.link-updated";
    public static final String BIOLINK_EVENT_DELETED                 = "bio.link-deleted";
    // ==================== BIO PAGE LOGS ====================
    // Bio Page Creation Logs
    public static final String BIOPAGE_CREATING_FOR_USERNAME         = "Creating bio page for username: {}, owner: {}";
    public static final String BIOPAGE_CREATE_SUCCESS                = "Successfully created bio page with ID: {}, username: {}";
    public static final String BIOPAGE_CREATE_FAILED                 = "Failed to create bio page for username: {}";
    public static final String BIOPAGE_USERNAME_CONFLICT_WARNING     = "Username conflict attempted: {}";
    // Bio Page Update Logs
    public static final String BIOPAGE_UPDATING_WITH_ID              = "Updating bio page with ID: {}";
    public static final String BIOPAGE_UPDATE_SUCCESS                = "Updated bio page with ID: {}";
    public static final String BIOPAGE_NOT_FOUND_WARNING             = "Attempted to update non-existent bio page with ID: {}";
    // Bio Page Deletion Logs
    public static final String BIOPAGE_DELETING_WITH_ID              = "Attempting to delete bio page with ID: {}";
    public static final String BIOPAGE_DELETE_SUCCESS                = "Successfully deleted bio page with ID: {}";
    public static final String BIOPAGE_DELETE_NOT_FOUND_WARNING      = "Attempted to delete non-existent bio page with ID: {}";
    // Bio Page Conversion Logs
    public static final String BIOPAGE_CONVERTING_TO_DTO             = "Converting BioPageEntity to DTO for ID: {}";
    public static final String BIOPAGE_CONVERT_TO_DTO_FAILED         = "Failed to convert BioPageEntity to DTO for ID: {}";
    // Bio Page Event Names
    public static final String BIOPAGE_EVENT_CREATED                 = "bio.page-created";
    public static final String BIOPAGE_EVENT_UPDATED                 = "bio.page-updated";
    public static final String BIOPAGE_EVENT_DELETED                 = "bio.page-deleted";
    // Bio Page Event Processing Logs
    public static final String BIOPAGE_EVENT_PROCESSING_FOR_USERNAME = "Processing bio page created event for username: {}, pageId: {}";
    public static final String BIOPAGE_EVENT_PROCESSING_SUCCESS      = "Successfully validated bio page: {} ({})";
    // Bio Page Analytics Logs
    public static final String BIOPAGE_ANALYTICS_NEW_PAGE_CREATED    = "Analytics: New bio page created - Username: {}, CreatedAt: {}, OwnerId: {}";
    // Bio Page Notification Logs
    public static final String BIOPAGE_NOTIFICATION_SENDING          = "Sending creation notification for page: {}";
    public static final String BIOPAGE_NOTIFICATION_SENT             = "Notification: Bio page created - Username: {}, Public URL: {}";
    // Bio Page Cache Logs
    public static final String BIOPAGE_CACHING_PAGE                  = "Caching bio page: {}";
    public static final String BIOPAGE_CACHE_PAGE_STORED             = "Cache: Bio page cached - Username: {}, PageId: {}";
    // Bio Page Debug Logs
    public static final String BIOPAGE_INITIALIZING_DEFAULT_LINKS    = "Initializing default bio links for page: {}";
    public static final String BIOPAGE_WELCOME_LINK_CREATED          = "Created welcome link for page: {}";
    public static final String BIOPAGE_ANALYTICS_UPDATING_NEW_PAGE   = "Updating analytics for new bio page: {}";
    // ==================== CAMPAIGN LOGS ====================
    // Campaign Creation Logs
    public static final String CAMPAIGN_QR_CREATED                   = "Created QR campaign: {} for user: {}";
    public static final String CAMPAIGN_GENERAL_CREATED              = "Created general campaign: {} for organization: {}";
    // Campaign Update Logs
    public static final String CAMPAIGN_UPDATED                      = "Updated campaign: {} by user: {}";
    // Campaign Deletion Logs
    public static final String CAMPAIGN_DELETED                      = "Deleted campaign: {} by user: {}";
    // Campaign Status Logs
    public static final String CAMPAIGN_STATUS_TOGGLED               = "Toggled status for campaign: {} to {} by user: {}";
    // ==================== REMAINING BIO PAGE LOGS ====================
    // Bio Page Deletion Details
    public static final String BIOPAGE_DELETING_WITH_LINKS           = "Deleting bio page '{}' with {} associated links";
    // Bio Page Event Processing
    public static final String BIOPAGE_USERNAME_MISMATCH_WARNING     = "Username mismatch in BioPageCreatedEvent. Event: {}, Actual: {}";
    public static final String BIOPAGE_VALIDATED_SUCCESS             = "Successfully validated bio page: {} ({})";
    public static final String BIOPAGE_DEFAULT_LINKS_INITIALIZED     = "Initialized default bio links for page: {}";
    public static final String BIOPAGE_EVENT_PROCESSED_SUCCESS       = "Successfully processed bio page created event for username: {}, pageId: {}";
    public static final String BIOPAGE_EVENT_PROCESSING_FAILED       = "Failed to process BioPageCreatedEvent for username: {}, pageId: {}";
    // ==================== QR RENDERER LOGS ====================
    // QR Rendering Process Logs
    public static final String QR_RENDERING_STARTING                 = "Starting QR rendering with config: {}";
    public static final String QR_ADDING_EXTRA_PIXELS                = "Adding {} extra pixels ({} each side) to achieve exact size";
    public static final String QR_RENDERING_MATRIX_INFO              = "Matrix size: {}, Image size: {}, Module size: {} (integer), Offset: X={}, Y={}";
    public static final String QR_RENDERING_COMPLETED                = "QR rendering completed successfully | Size: {}px | Shapes: {}/{}";
    // QR Drawing Logs
    public static final String QR_DRAWING_BACKGROUND                 = "Drawing background with color: {}";
    public static final String QR_DRAWING_BODY_MODULES               = "Drew {} body modules";
    public static final String QR_DRAWING_STYLED_MODULES             = "Drew {} modules (styled body mode)";
    // QR Logo and Asset Logs
    public static final String QR_LOGO_LOAD_FAILED                   = "Could not load logo from path: {}";
    public static final String QR_LOGO_DRAW_ERROR                    = "Error drawing logo";
    // QR Color and Style Logs
    public static final String QR_INVALID_COLOR_WARNING              = "Invalid color: {}, using black instead";
    // ==================== GRPC SERVICE LOGS ====================
    // gRPC URL Retrieval Logs
    public static final String GRPC_GETTING_URLS_FOR_USER            = "gRPC: Getting URLs for user: {}";
    public static final String GRPC_RETRIEVED_URLS_FOR_USER          = "gRPC: Retrieved {} URLs for user: {}";
    public static final String GRPC_ERROR_GETTING_URLS_FOR_USER      = "gRPC: Error getting URLs for user: {}";
    // gRPC URL by ID Logs
    public static final String GRPC_GETTING_URL_BY_ID                = "gRPC: Getting URL by ID: {}";
    public static final String GRPC_RETRIEVED_URL                    = "gRPC: Retrieved URL: {}";
    public static final String GRPC_ERROR_GETTING_URL_BY_ID          = "gRPC: Error getting URL by ID: {}";
    // gRPC URLs by Status Logs
    public static final String GRPC_GETTING_URLS_BY_STATUS           = "gRPC: Getting URLs by status: {} for user: {}";
    public static final String GRPC_RETRIEVED_URLS_BY_STATUS         = "gRPC: Retrieved {} URLs with status: {} for user: {}";
    public static final String GRPC_ERROR_GETTING_URLS_BY_STATUS     = "gRPC: Error getting URLs by status: {} for user: {}";
    // ==================== QR SERVICE LOGS ====================
    // QR Generation Error Logs
    public static final String QR_SERVICE_GENERATION_FAILED          = "Failed to generate styled QR code";
}