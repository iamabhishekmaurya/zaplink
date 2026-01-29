package io.zaplink.media.common.constants;

/**
 * Logging-related constants for log message templates and logging patterns.
 */
public final class LogConstants
{
    // Prevent instantiation
    private LogConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== TRACE ID LOGS ====================
    public static final String LOG_TRACE_ID_SET_FOR_THREAD       = "Trace ID set for current thread: {}";
    public static final String LOG_TRACE_ID_CLEARED_FOR_THREAD   = "Trace ID cleared for current thread: {}";
    public static final String LOG_TRACE_ID_FOUND_IN_HEADER      = "Trace ID found in header: {}";
    public static final String LOG_TRACE_ID_GENERATED_NEW        = "Generated new trace ID: {}";
    public static final String LOG_REQUEST_STARTED_WITH_TRACE    = "Request started - Method: {}, URI: {}, TraceId: {}";
    public static final String LOG_REQUEST_COMPLETED_WITH_TRACE  = "Request completed - Method: {}, URI: {}, TraceId: {}, Status: {}";
    // ==================== MEDIA SERVICE LOGS ====================
    public static final String LOG_UPLOAD_START                  = "Starting upload for file: {}, owner: {}, content-type: {}";
    public static final String LOG_IMAGE_METADATA_PROCESSING     = "Processing image metadata for: {}";
    public static final String LOG_IMAGE_DIMENSIONS              = "Image dimensions: {}x{}";
    public static final String LOG_IMAGE_METADATA_FAILED         = "Failed to process image metadata/thumbnail for file: {}. Proceeding with original upload.";
    public static final String LOG_STORAGE_UPLOAD_START          = "Uploading file to storage with key: {}";
    public static final String LOG_STORAGE_UPLOAD_FAILED         = "Storage upload failed for key: {}";
    public static final String LOG_FOLDER_ASSOCIATION            = "Associating asset with folder: {}";
    public static final String LOG_ASSET_SAVED                   = "Asset saved to database. ID: {}";
    public static final String LOG_EVENT_PUBLISHED               = "Published media-events for asset: {}";
    public static final String LOG_EVENT_PUBLISH_FAILED          = "Failed to publish media event for asset: {}";
    public static final String LOG_ASSET_DELETING                = "Deleting asset: {}";
    public static final String LOG_STORAGE_KEY_EXTRACTED         = "Extracted storage key: {}";
    public static final String LOG_STORAGE_KEY_EXTRACTION_FAILED = "Could not extract storage key from URL: {}. Storage deletion skipped.";
    public static final String LOG_THUMBNAIL_DELETING            = "Deleting thumbnail: {}";
    public static final String LOG_STORAGE_DELETE_FAILED         = "Failed to delete file from storage for asset: {}";
    public static final String LOG_ASSET_DELETED_DB              = "Asset deleted from database: {}";
    public static final String LOG_THUMBNAIL_GENERATION          = "Generating thumbnail for {}. Target size: {}x{}";
    public static final String LOG_THUMBNAIL_UPLOAD_START        = "Uploading thumbnail to storage key: {}";
    public static final String LOG_THUMBNAIL_UPLOAD_SUCCESS      = "Thumbnail uploaded successfully: {}";
    public static final String LOG_THUMBNAIL_GENERATION_FAILED   = "Failed to generate thumbnail for {}: {}";
    // ==================== STORAGE SERVICE LOGS ====================
    public static final String LOG_BUCKET_EXISTS                 = "S3 Bucket '{}' exists and is accessible.";
    public static final String LOG_BUCKET_NOT_FOUND              = "Bucket '{}' does not exist. Attempting to create...";
    public static final String LOG_BUCKET_CREATED                = "Bucket '{}' created successfully.";
    public static final String LOG_BUCKET_CREATION_FAILED        = "Failed to create bucket '{}'. Ensure S3/MinIO credentials have create permissions.";
    public static final String LOG_BUCKET_ACCESS_ERROR           = "Error accessing S3 bucket '{}': {}";
    public static final String LOG_UPLOAD_DELEGATION             = "Delegating upload for file: {} to key: {}";
    public static final String LOG_STREAM_UPLOAD_START           = "Uploading stream to S3. Key: {}, Size: {}, Type: {}";
    public static final String LOG_S3_UPLOAD_SUCCESS             = "Successfully uploaded object to S3: {}";
    public static final String LOG_S3_UPLOAD_FAILED              = "S3 Upload failed for key: {}";
    public static final String LOG_S3_DELETE_START               = "Deleting object from S3: {}";
    public static final String LOG_S3_DELETE_SENT                = "Delete request sent for key: {}";
    public static final String LOG_S3_DELETE_FAILED              = "Failed to delete object: {}";
    // ==================== CONTROLLER LOGS ====================
    public static final String LOG_CONTROLLER_UPLOAD_REQ         = "Received upload request. Owner: {}, Folder: {}, File: {}";
    public static final String LOG_CONTROLLER_UPLOAD_SUCCESS     = "Upload successful. Asset ID: {}";
    public static final String LOG_CONTROLLER_LIST_REQ           = "Listing assets. Owner: {}, Folder: {}, Page: {}";
    public static final String LOG_CONTROLLER_DELETE_REQ         = "Received delete request for asset ID: {}";
}
