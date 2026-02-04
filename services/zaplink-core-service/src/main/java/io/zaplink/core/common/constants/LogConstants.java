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
    public static final String LOG_TRACE_ID_SET_FOR_THREAD        = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD    = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER       = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW         = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE     = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE   = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== EXCEPTION HANDLING LOGS ====================
    public static final String LOG_AUTH_EXCEPTION_OCCURRED        = "AuthException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_ALREADY_EXISTS_EXCEPTION  = "UserAlreadyExistsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_USER_NOT_FOUND_EXCEPTION       = "UserNotFoundException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_INVALID_CREDENTIALS_EXCEPTION  = "InvalidCredentialsException occurred - TraceId: {}, Error: {}, Code: {}";
    public static final String LOG_AUTHENTICATION_EXCEPTION       = "AuthenticationException occurred - TraceId: {}, Error: {}";
    public static final String LOG_BAD_CREDENTIALS_EXCEPTION      = "BadCredentialsException occurred - TraceId: {}, Error: {}";
    public static final String LOG_ACCESS_DENIED_EXCEPTION        = "AccessDeniedException occurred - TraceId: {}, Error: {}";
    public static final String LOG_VALIDATION_EXCEPTION_OCCURRED  = "MethodArgumentNotValidException occurred - TraceId: {}, Validation errors: {}";
    public static final String LOG_UNEXPECTED_EXCEPTION_OCCURRED  = "Unexpected exception occurred - TraceId: {}, Error: {}";
    // ==================== URL SHORTENER LOGS ====================
    public static final String LOG_SHORT_URL_INIT                 = "Initializing short URL creation";
    public static final String LOG_URL_MAPPING_CREATED            = "URL mapping created successfully";
    public static final String LOG_URL_MAPPING_NOT_CREATED        = "URL mapping creation failed";
    public static final String LOG_URL_SHORTENING_EXCEPTION       = "Exception during URL shortening: {}";
    public static final String LOG_CREATING_URL_MAPPING           = "Creating URL mapping with key: {}";
    public static final String LOG_UPDATING_SHORT_URL             = "Updating short URL for key: {}";
    public static final String LOG_URL_MAPPING_UPDATED            = "URL mapping updated successfully for key: {}";
    // ==================== EVENT PUBLISHING LOGS ====================
    public static final String EVENT_PUBLISHING                   = "Publishing event: {}";
    public static final String EVENT_PUBLISHED                    = "Event published successfully: {}";
    public static final String EVENT_PUBLISH_ERROR                = "Failed to publish event: {}";
    // ==================== TEAM MANAGEMENT LOGS ====================
    public static final String TEAM_MEMBER_INVITING               = "Inviting team member: {}";
    public static final String TEAM_MEMBER_INVITED                = "Team member invited successfully: {}";
    public static final String TEAM_MEMBER_ROLE_CHANGING          = "Changing team member role: {}";
    public static final String TEAM_MEMBER_ROLE_CHANGED           = "Team member role changed successfully: {}";
    public static final String TEAM_MEMBER_REMOVING               = "Removing team member: {}";
    public static final String TEAM_MEMBER_REMOVED                = "Team member removed successfully: {}";
    // ==================== WORKFLOW LOGS ====================
    public static final String POST_SUBMITTING                    = "Submitting post for approval: {}";
    public static final String POST_SUBMITTED                     = "Post submitted for approval successfully: {}";
    public static final String POST_REVIEWING                     = "Reviewing post: {}";
    public static final String POST_REVIEWED                      = "Post reviewed successfully: {}";
    // ==================== DYNAMIC QR LOGS ====================
    public static final String DYNAMIC_QR_CREATING                = "Creating dynamic QR code. Name: {}, User: {}";
    public static final String DYNAMIC_QR_CREATED                 = "Created dynamic QR with key: {} for user: {}";
    public static final String DYNAMIC_QR_UPDATED                 = "Updated dynamic QR with key: {} for user: {}";
    public static final String DYNAMIC_QR_UPDATING_DESTINATION    = "Updated destination for QR key: {} by user: {}";
    public static final String DYNAMIC_QR_TOGGLING_STATUS         = "Toggled status for QR key: {} to {} by user: {}";
    public static final String DYNAMIC_QR_DELETING                = "Deleted dynamic QR with key: {} by user: {}";
    public static final String DYNAMIC_QR_ERROR_CONVERTING_CONFIG = "Error converting QR config to JSON";
    public static final String DYNAMIC_QR_ERROR_PARSING_JSON      = "Error parsing JSON fields for response";
    // ==================== QR GENERATION LOGS ====================
    public static final String QR_ZXING_MARGIN_HINT_SET           = "ZXing margin hint set to: {}";
    public static final String QR_GENERATED_MATRIX                = "Generated QR matrix: {}x{} for data: {}";
    public static final String QR_ZXING_ENFORCED_MARGIN           = "ZXing enforced additional margin: {} (requested: {})";
    public static final String QR_CROPPING_MATRIX                 = "Cropping matrix to remove ZXing enforced margin...";
    public static final String QR_CROPPED_MATRIX_SIZE             = "Cropped matrix size: {}x{}";
    public static final String QR_FAILED_TO_GENERATE_ADVANCED     = "Failed to generate advanced QR";
    public static final String QR_CANNOT_CROP_MATRIX              = "Cannot crop matrix - new size would be {}x{}";
    // ==================== QR VERIFICATION LOGS ====================
    public static final String QR_VERIFICATION_SUCCESS            = "QR decoded successfully! Original: {}, Decoded: {}";
    public static final String QR_NATIVE_VERIFICATION_SUCCESS     = "NATIVE QR decoded! Original: {}, Decoded: {}";
    public static final String QR_STYLED_VERIFICATION_SUCCESS     = "STYLED QR decoded! bodyShape: {}, eyeShape: {}, Decoded: {}";
    public static final String QR_VERIFICATION_FAILED             = "QR verification failed";
    public static final String QR_NATIVE_VERIFICATION_FAILED      = "NATIVE QR verification failed";
    public static final String QR_STYLED_VERIFICATION_FAILED      = "STYLED QR verification failed for body: {}, eye: {}";
    // ==================== SUCCESS LOGS ====================
    public static final String SUCCESS_QR_DECODED                 = "QR decoded successfully! Original: {}, Decoded: {}";
    public static final String SUCCESS_NATIVE_QR_DECODED          = "NATIVE QR decoded! Original: {}, Decoded: {}";
    public static final String SUCCESS_STYLED_QR_DECODED          = "STYLED QR decoded! bodyShape: {}, eyeShape: {}, Decoded: {}";
}