package io.zaplink.core.common.constants;

/**
 * Application message constants for error messages, status values, and other text messages.
 * Contains all user-facing and system messages used throughout the application.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public final class MessageConstants
{
    // Prevent instantiation
    private MessageConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== ENCODING CONSTANTS ====================
    public static final String ENCODING_UTF_8                = "UTF-8";
    // ==================== TEAM MEMBER STATUS & ROLES ====================
    public static final String STATUS_ACTIVE                 = "ACTIVE";
    public static final String STATUS_INACTIVE               = "INACTIVE";
    public static final String STATUS_PENDING                = "PENDING";
    public static final String ROLE_ADMIN                    = "ADMIN";
    public static final String ROLE_EDITOR                   = "EDITOR";
    public static final String ROLE_APPROVER                 = "APPROVER";
    public static final String ROLE_VIEWER                   = "VIEWER";
    public static final String ROLE_INFLUENCER               = "INFLUENCER";
    // ==================== POST STATUS & DECISIONS ====================
    public static final String POST_STATUS_DRAFT             = "DRAFT";
    public static final String POST_STATUS_SUBMITTED         = "SUBMITTED";
    public static final String POST_STATUS_APPROVED          = "APPROVED";
    public static final String POST_STATUS_REJECTED          = "REJECTED";
    public static final String POST_STATUS_PUBLISHED         = "PUBLISHED";
    public static final String DECISION_APPROVE              = "APPROVE";
    public static final String DECISION_REJECT               = "REJECT";
    // ==================== API RESPONSE KEYS ====================
    public static final String RESPONSE_KEY_SUCCESS          = "success";
    public static final String RESPONSE_KEY_ORIGINAL_DATA    = "originalData";
    public static final String RESPONSE_KEY_DECODED_DATA     = "decodedData";
    public static final String RESPONSE_KEY_MATCH            = "match";
    public static final String RESPONSE_KEY_ERROR            = "error";
    public static final String RESPONSE_KEY_RENDERER         = "renderer";
    public static final String RESPONSE_KEY_BODY_SHAPE       = "bodyShape";
    public static final String RESPONSE_KEY_EYE_SHAPE        = "eyeShape";
    // ==================== FIELD NAMES ====================
    public static final String FIELD_GLOBAL                  = "global";
    // ==================== SEPARATORS ====================
    public static final String SEPARATOR_COMMA               = ",";
    public static final String SEPARATOR_URI_EQUALS          = "uri=";
    // ==================== RENDERER TYPES ====================
    public static final String RENDERER_NATIVE_ZXING         = "NATIVE_ZXING";
    public static final String RENDERER_STYLED               = "STYLED";
    public static final String RENDERER_ADVANCED             = "ADVANCED";
    // ==================== HTTP HEADERS ====================
    public static final String HEADER_ACCEPT                 = "Accept";
    public static final String HEADER_CONTENT_TYPE           = "Content-Type";
    // ==================== MEDIA TYPES ====================
    public static final String MEDIA_TYPE_APPLICATION_JSON   = "application/json";
    public static final String MEDIA_TYPE_TEXT_PLAIN         = "text/plain";
    public static final String MEDIA_TYPE_OPENMETRICS_TEXT   = "application/openmetrics-text";
    // ==================== DECISION DESCRIPTIONS ====================
    public static final String DECISION_DESCRIPTION_APPROVED = "Approved";
    public static final String DECISION_DESCRIPTION_REJECTED = "Rejected";
}
