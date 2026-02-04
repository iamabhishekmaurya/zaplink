package io.zaplink.core.common.constants;

/**
 * Error-related constants for error messages and validation messages.
 * Contains all error message templates used throughout the application.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-26
 */
public final class ErrorConstant
{
    // Prevent instantiation
    private ErrorConstant()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== GENERAL ERRORS ====================
    public static final String ERROR_VALIDATION_FAILED                                                 = "Validation failed";
    public static final String ERROR_INTERNAL_SERVER_ERROR                                             = "Internal server error";
    public static final String ERROR_RESOURCE_NOT_FOUND                                                = "Resource not found: {}";
    public static final String ERROR_BUSINESS_RULE_VIOLATION                                           = "Business rule violation: {}";
    // ==================== URL SHORTENER ERRORS ====================
    public static final String ORIGINAL_URL_REQUIRED                                                   = "Original URL is required";
    public static final String SHORT_URL_REQUIRED                                                      = "Short URL is required";
    public static final String URL_FORMAT_INVALID                                                      = "Invalid URL format";
    public static final String SHORT_URL_NOT_FOUND                                                     = "Short URL not found";
    public static final String TITLE_LENGTH_EXCEEDED                                                   = "Title cannot exceed 100 characters";
    public static final String PLATFORM_LENGTH_EXCEEDED                                                = "Platform cannot exceed 50 characters";
    public static final String TAGS_SIZE_EXCEEDED                                                      = "Tags list cannot exceed 5 items";
    public static final String TAG_LENGTH_EXCEEDED                                                     = "Tag cannot exceed 20 characters";
    public static final String ERROR_LINK_NOT_FOUND_WITH_KEY                                           = "Link not found with key: {}";
    // ==================== TRACE ID ERRORS ====================
    public static final String TRACE_ID_LENGTH_ERROR                                                   = "Trace ID must be at most 32 characters";
    public static final String TRACE_ID_CONTAINS_ALPHANUMERIC_AND_HYPHAN                               = "Trace ID must contain only alphanumeric characters and hyphens";
    // ==================== DYNAMIC QR ERRORS ====================
    public static final String ERROR_DYNAMIC_QR_NOT_FOUND_OR_ACCESS_DENIED                             = "Dynamic QR not found or access denied";
    public static final String ERROR_FAILED_TO_PROCESS_QR_CONFIG                                       = "Failed to process QR configuration";
    public static final String ERROR_ADV_QR_GENERATION_FAILED                                          = "Adv QR Generation failed";
    public static final String ERROR_QR_NAME_REQUIRED                                                  = "QR name is required";
    public static final String ERROR_QR_NAME_TOO_LONG                                                  = "QR name must not exceed {} characters";
    public static final String ERROR_DESTINATION_URL_REQUIRED                                          = "Destination URL is required";
    public static final String ERROR_DESTINATION_URL_TOO_LONG                                          = "Destination URL must not exceed {} characters";
    public static final String ERROR_QR_CONFIG_REQUIRED                                                = "QR configuration is required";
    // ==================== WORKFLOW ERRORS ====================
    public static final String ERROR_AUTHOR_NOT_ACTIVE_TEAM_MEMBER                                     = "Author is not an active team member";
    public static final String ERROR_REVIEWER_NOT_ACTIVE_APPROVER                                      = "Reviewer is not an active team member with approver role";
    public static final String ERROR_POST_NOT_FOUND                                                    = "Post not found: {}";
    public static final String ERROR_POST_NOT_SUBMITTED_STATUS                                         = "Post is not in submitted status";
    public static final String ERROR_DECISION_MUST_BE_APPROVE_OR_REJECT                                = "Decision must be either APPROVE or REJECT";
    public static final String ERROR_POST_ID_MUST_BE_POSITIVE                                          = "Post ID must be positive";
    public static final String ERROR_REVIEWER_ID_MUST_BE_POSITIVE                                      = "Reviewer ID must be positive";
    public static final String ERROR_COMMENTS_CANNOT_EXCEED_1000_CHARACTERS                            = "Comments cannot exceed 1000 characters";
    // ==================== TEAM MANAGEMENT ERRORS ====================
    public static final String ERROR_USER_ID_MUST_BE_POSITIVE                                          = "User ID must be positive";
    public static final String ERROR_TEAM_ID_MUST_BE_POSITIVE                                          = "Team ID must be positive";
    public static final String ERROR_REASON_LENGTH_TOO_LONG                                            = "Reason must be less than 500 characters";
    public static final String ERROR_ROLE_NOT_ALLOWED                                                  = "Role must be one of: ADMIN, EDITOR, APPROVER, VIEWER, INFLUENCER";
    // ==================== VALIDATION ERRORS ====================
    public static final String ERROR_SORT_ORDER_CANNOT_BE_NEGATIVE                                     = "Sort order cannot be negative";
    public static final String ERROR_LINK_ORDERS_CANNOT_BE_NULL_OR_EMPTY                               = "Link orders cannot be null or empty";
    public static final String ERROR_DUPLICATE_LINK_IDS_NOT_ALLOWED                                    = "Duplicate link IDs are not allowed";
    public static final String ERROR_DUPLICATE_SORT_ORDERS_NOT_ALLOWED                                 = "Duplicate sort orders are not allowed";
    public static final String ERROR_SORT_ORDERS_MUST_FORM_VALID_SEQUENCE                              = "Sort orders must form a valid sequence starting from 0";
    // ==================== USER/ACCOUNT ERRORS ====================
    public static final String ERROR_USERNAME_CANNOT_BE_NULL                                           = "Username cannot be null";
    public static final String ERROR_USERNAME_CANNOT_BE_EMPTY                                          = "Username cannot be empty";
    public static final String ERROR_USERNAME_FORMAT                                                   = "Username can only contain letters, numbers, underscores, and hyphens";
    public static final String ERROR_USERNAME_CANNOT_START_WITH_UNDERSCORE_OR_HYPHEN                   = "Username cannot start with underscore or hyphen";
    public static final String ERROR_USERNAME_REQUIRED                                                 = "Username is required";
    public static final String ERROR_USERNAME_LENGTH                                                   = "Username must be between {} and {} characters";
    public static final String ERROR_USERNAME_START_CHARACTER                                          = "Username cannot start with underscore or hyphen";
    public static final String ERROR_USERNAME_EMPTY                                                    = "Username cannot be empty";
    // ==================== URL/BIO LINK ERRORS ====================
    public static final String ERROR_URL_CANNOT_BE_EMPTY                                               = "URL cannot be empty";
    public static final String ERROR_URL_FORMAT                                                        = "URL must start with http:// or https://";
    public static final String ERROR_URL_TOO_LONG                                                      = "URL must be less than {} characters";
    public static final String ERROR_EMAIL_FORMAT                                                      = "Invalid email format";
    public static final String ERROR_PHONE_FORMAT                                                      = "Invalid phone number format";
    public static final String ERROR_PRICE_NEGATIVE                                                    = "Price cannot be negative for PRODUCT links";
    public static final String ERROR_CURRENCY_LENGTH                                                   = "Currency must be a 3-letter code for PRODUCT links";
    public static final String ERROR_UNKNOWN_LINK_TYPE                                                 = "Unknown link type: {}";
    // ==================== SYSTEM ERRORS ====================
    public static final String ERROR_CLOCK_MOVED_BACKWARDS                                             = "Clock moved backwards. Refusing to generate id.";
    public static final String ERROR_MACHINE_ID_RANGE                                                  = "Machine ID must be between 0 and {}";
    // ==================== VALIDATION MESSAGE CONSTANTS ====================
    // General validation
    public static final String VALIDATION_REQUIRED                                                     = "is required";
    public static final String VALIDATION_NOT_BLANK                                                    = "is required";
    public static final String VALIDATION_INVALID_EMAIL                                                = "Invalid email format";
    public static final String VALIDATION_SIZE_EXCEEDED                                                = "must not exceed {} characters";
    public static final String VALIDATION_SIZE_RANGE                                                   = "must be between {} and {} characters";
    public static final String VALIDATION_MIN_VALUE                                                    = "must be non-negative";
    public static final String VALIDATION_NOT_EMPTY                                                    = "At least one link order is required";
    // Field-specific validation
    public static final String VALIDATION_TITLE_REQUIRED                                               = "Title is required";
    public static final String VALIDATION_AUTHOR_ID_REQUIRED                                           = "Author ID is required";
    public static final String VALIDATION_EMAIL_REQUIRED                                               = "Email is required";
    public static final String VALIDATION_ROLE_REQUIRED                                                = "Role is required";
    public static final String VALIDATION_TEAM_ID_REQUIRED                                             = "Team ID is required";
    public static final String VALIDATION_USER_ID_REQUIRED                                             = "User ID is required";
    public static final String VALIDATION_NEW_ROLE_REQUIRED                                            = "New role is required";
    public static final String VALIDATION_POST_ID_REQUIRED                                             = "Post ID is required";
    public static final String VALIDATION_DECISION_REQUIRED                                            = "Decision is required";
    public static final String VALIDATION_REVIEWER_ID_REQUIRED                                         = "Reviewer ID is required";
    public static final String VALIDATION_QR_NAME_REQUIRED                                             = "QR Name is required";
    public static final String VALIDATION_DESTINATION_URL_REQUIRED                                     = "Destination URL is required";
    public static final String VALIDATION_QR_CONFIG_REQUIRED                                           = "QR configuration is required";
    public static final String VALIDATION_USERNAME_REQUIRED                                            = "Username is required";
    public static final String VALIDATION_OWNER_ID_REQUIRED                                            = "Owner ID is required";
    public static final String VALIDATION_PAGE_ID_REQUIRED                                             = "Page ID is required";
    public static final String VALIDATION_LINK_ID_REQUIRED                                             = "Link ID is required";
    public static final String VALIDATION_SORT_ORDER_REQUIRED                                          = "Sort order is required";
    public static final String VALIDATION_DIMENSION_REQUIRED                                           = "Dimension is required";
    public static final String VALIDATION_VALUE_REQUIRED                                               = "Value is required";
    public static final String VALIDATION_PRIORITY_REQUIRED                                            = "Priority is required";
    public static final String VALIDATION_SHORT_URL_KEY_REQUIRED                                       = "Short URL Key is required";
    // Size validation messages
    public static final String VALIDATION_QR_NAME_MAX_LENGTH                                           = "QR name must not exceed 255 characters";
    public static final String VALIDATION_DESTINATION_URL_MAX_LENGTH                                   = "Destination URL must not exceed 2048 characters";
    public static final String VALIDATION_USERNAME_SIZE_RANGE                                          = "Username must be between 3 and 50 characters";
    public static final String VALIDATION_AVATAR_URL_MAX_LENGTH                                        = "Avatar URL must be less than 500 characters";
    public static final String VALIDATION_BIO_TEXT_MAX_LENGTH                                          = "Bio text must be less than 500 characters";
    public static final String VALIDATION_TITLE_MAX_LENGTH                                             = "Title must be less than 200 characters";
    public static final String VALIDATION_URL_MAX_LENGTH                                               = "URL must be less than 2048 characters";
    public static final String VALIDATION_TYPE_REQUIRED                                                = "Type is required";
    // ==================== BUSINESS LOGIC ERROR CONSTANTS ====================
    public static final String ERROR_BIOPAGE_CREATED_EVENT_CANNOT_BE_NULL                              = "BioPageCreatedEvent cannot be null";
    public static final String ERROR_BIOPAGE_NOT_FOUND_WITH_ID                                         = "BioPage not found with ID: {}";
    public static final String ERROR_USERNAME_MISMATCH                                                 = "Username does not match between event and bio page";
    public static final String ERROR_TITLE_IS_REQUIRED                                                 = "Title is required";
    public static final String ERROR_URL_REQUIRED_FOR_LINK_TYPE                                        = "URL is required for {} links";
    public static final String ERROR_PRODUCT_LINKS_MUST_HAVE_PRICE_AND_CURRENCY                        = "Product links must have both price and currency";
    public static final String ERROR_TITLE_CANNOT_BE_NULL                                              = "Title cannot be null";
    public static final String ERROR_LINK_TYPE_CANNOT_BE_NULL                                          = "Link type cannot be null";
    public static final String ERROR_WEBSITE_LINKS_MUST_HAVE_VALID_URL                                 = "Website links must have a valid URL";
    public static final String ERROR_SOCIAL_MEDIA_LINKS_MUST_HAVE_VALID_URL                            = "Social media links must have a valid URL";
    public static final String ERROR_SORT_ORDER_MUST_BE_NON_NEGATIVE                                   = "Sort order must be a non-negative integer";
    public static final String ERROR_PRODUCT_LINKS_MUST_HAVE_PRICE                                     = "Product links must have a price";
    public static final String ERROR_PRODUCT_PRICE_CANNOT_BE_NEGATIVE                                  = "Product price cannot be negative";
    public static final String ERROR_PRODUCT_LINKS_MUST_HAVE_CURRENCY                                  = "Product links must have a currency";
    public static final String ERROR_OWNER_ID_CANNOT_BE_NULL_OR_EMPTY                                  = "Owner ID cannot be null or empty";
    public static final String ERROR_PRODUCT_PRICE_MUST_BE_POSITIVE                                    = "Product price must be positive";
    public static final String ERROR_CURRENCY_MUST_BE_VALID_ISO_CODE                                   = "Currency must be a valid 3-letter ISO code";
    public static final String ERROR_USERNAME_MUST_BE_AT_LEAST_3_CHARACTERS                            = "Username must be at least 3 characters long";
    public static final String ERROR_USERNAME_CANNOT_EXCEED_50_CHARACTERS                              = "Username cannot exceed 50 characters";
    public static final String ERROR_USERNAME_CAN_ONLY_CONTAIN_LETTERS_NUMBERS_UNDERSCORES_AND_HYPHENS = "Username can only contain letters, numbers, underscores, and hyphens";
    public static final String ERROR_TITLE_CANNOT_BE_EMPTY                                             = "Title cannot be empty";
    public static final String ERROR_TITLE_MUST_BE_AT_LEAST_1_CHARACTER_LONG                           = "Title must be at least 1 character long";
    public static final String ERROR_TITLE_CANNOT_EXCEED_200_CHARACTERS                                = "Title cannot exceed 200 characters";
    public static final String ERROR_USERNAME_ALREADY_EXISTS                                           = "Username '{}' already exists";
    public static final String ERROR_BIO_PAGE_NOT_FOUND_WITH_ID                                        = "Bio page not found with ID: {}";
    public static final String ERROR_TEAM_NOT_FOUND                                                    = "Team not found: {}";
    public static final String ERROR_USER_ALREADY_MEMBER_OF_TEAM                                       = "User is already a member of this team";
    public static final String ERROR_TEAM_MEMBER_NOT_FOUND                                             = "Team member not found: {}";
    public static final String ERROR_TEAM_MEMBER_NOT_FOUND_SIMPLE                                      = "Team member not found";
    public static final String ERROR_URL_NOT_FOUND                                                     = "URL not found: {}";
    public static final String ERROR_STYLED_QR_CODE_GENERATION_FAILED                                  = "Styled QR code generation failed";
    public static final String ERROR_FAILED_TO_PUBLISH_TEAM_MEMBER_ADDED_EVENT                         = "Failed to publish team member added event";
    public static final String ERROR_FAILED_TO_PUBLISH_WORKFLOW_STATUS_CHANGED_EVENT                   = "Failed to publish workflow status changed event";
}
