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
    public static final String ENCODING_UTF_8                                   = "UTF-8";
    // ==================== TEAM MEMBER STATUS & ROLES ====================
    public static final String STATUS_ACTIVE                                    = "ACTIVE";
    public static final String STATUS_INACTIVE                                  = "INACTIVE";
    public static final String STATUS_PENDING                                   = "PENDING";
    public static final String ROLE_ADMIN                                       = "ADMIN";
    public static final String ROLE_EDITOR                                      = "EDITOR";
    public static final String ROLE_APPROVER                                    = "APPROVER";
    public static final String ROLE_VIEWER                                      = "VIEWER";
    public static final String ROLE_INFLUENCER                                  = "INFLUENCER";
    // ==================== POST STATUS & DECISIONS ====================
    public static final String POST_STATUS_DRAFT                                = "DRAFT";
    public static final String POST_STATUS_SUBMITTED                            = "SUBMITTED";
    public static final String POST_STATUS_APPROVED                             = "APPROVED";
    public static final String POST_STATUS_REJECTED                             = "REJECTED";
    public static final String POST_STATUS_PUBLISHED                            = "PUBLISHED";
    public static final String DECISION_APPROVE                                 = "APPROVE";
    public static final String DECISION_REJECT                                  = "REJECT";
    // ==================== BIO LINK MESSAGES ====================
    // Bio Link Error Messages
    public static final String ERROR_BIO_LINK_NOT_FOUND_WITH_ID                 = "Bio link not found with ID: %d";
    public static final String ERROR_PRODUCT_LINKS_MUST_HAVE_PRICE_AND_CURRENCY = "Product links must have both price and currency";
    public static final String ERROR_LINK_DOES_NOT_BELONG_TO_PAGE               = "Link ID %d does not belong to page ID %d";
    public static final String ERROR_LINK_NOT_FOUND_WITH_ID                     = "Link not found with ID: %d";
    public static final String ERROR_FAILED_TO_CREATE_BIO_LINK                  = "Failed to create bio link";
    public static final String ERROR_FAILED_TO_CONVERT_ENTITY_TO_DTO            = "Failed to convert entity to DTO";
    // Bio Link Type Constants
    public static final String LINK_TYPE_PRODUCT                                = "PRODUCT";
    public static final String LINK_TYPE_LINK                                   = "LINK";
    public static final String LINK_TYPE_SOCIAL                                 = "SOCIAL";
    public static final String LINK_TYPE_EMAIL                                  = "EMAIL";
    public static final String LINK_TYPE_PHONE                                  = "PHONE";
    public static final String LINK_TYPE_EMBED                                  = "EMBED";
    public static final String LINK_TYPE_SCHEDULED                              = "SCHEDULED";
    public static final String LINK_TYPE_GATED                                  = "GATED";
    public static final String LINK_TYPE_PAYMENT                                = "PAYMENT";
    // ==================== BIO PAGE MESSAGES ====================
    // Bio Page Error Messages
    public static final String ERROR_BIO_PAGE_NOT_FOUND_WITH_ID                 = "Bio page not found with ID: %d";
    public static final String ERROR_FAILED_TO_CREATE_BIO_PAGE                  = "Failed to create bio page";
    public static final String ERROR_BIOPAGE_CREATED_EVENT_CANNOT_BE_NULL       = "BioPageCreatedEvent cannot be null";
    public static final String ERROR_USERNAME_MISMATCH                          = "Username mismatch in BioPageCreatedEvent";
    // Bio Page Welcome Link Constants
    public static final String WELCOME_LINK_TITLE                               = "Welcome to my page!";
    public static final String WELCOME_LINK_URL                                 = "https://zaplink.io";
    // ==================== API RESPONSE KEYS ====================
    public static final String RESPONSE_KEY_SUCCESS                             = "success";
    public static final String RESPONSE_KEY_ORIGINAL_DATA                       = "originalData";
    public static final String RESPONSE_KEY_DECODED_DATA                        = "decodedData";
    public static final String RESPONSE_KEY_MATCH                               = "match";
    public static final String RESPONSE_KEY_ERROR                               = "error";
    public static final String RESPONSE_KEY_RENDERER                            = "renderer";
    public static final String RESPONSE_KEY_BODY_SHAPE                          = "bodyShape";
    public static final String RESPONSE_KEY_EYE_SHAPE                           = "eyeShape";
    // ==================== FIELD NAMES ====================
    public static final String FIELD_GLOBAL                                     = "global";
    // ==================== SEPARATORS ====================
    public static final String SEPARATOR_COMMA                                  = ",";
    public static final String SEPARATOR_URI_EQUALS                             = "uri=";
    // ==================== RENDERER TYPES ====================
    public static final String RENDERER_NATIVE_ZXING                            = "NATIVE_ZXING";
    public static final String RENDERER_STYLED                                  = "STYLED";
    public static final String RENDERER_ADVANCED                                = "ADVANCED";
    // ==================== HTTP HEADERS ====================
    public static final String HEADER_ACCEPT                                    = "Accept";
    public static final String HEADER_CONTENT_TYPE                              = "Content-Type";
    // ==================== MEDIA TYPES ====================
    public static final String MEDIA_TYPE_APPLICATION_JSON                      = "application/json";
    public static final String MEDIA_TYPE_TEXT_PLAIN                            = "text/plain";
    public static final String MEDIA_TYPE_OPENMETRICS_TEXT                      = "application/openmetrics-text";
    // ==================== DECISION DESCRIPTIONS ====================
    public static final String DECISION_DESCRIPTION_APPROVED                    = "Approved";
    public static final String DECISION_DESCRIPTION_REJECTED                    = "Rejected";
}
