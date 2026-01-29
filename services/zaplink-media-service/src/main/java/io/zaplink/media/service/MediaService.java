package io.zaplink.media.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.media.common.constants.AppConstants;
import io.zaplink.media.common.constants.ExceptionConstants;
import io.zaplink.media.common.constants.LogConstants;
import io.zaplink.media.common.exception.AssetNotFoundException;
import io.zaplink.media.common.exception.StorageException;
import io.zaplink.media.dto.event.MediaUploadedEvent;
import io.zaplink.media.entity.Asset;
import io.zaplink.media.repository.AssetRepository;
import io.zaplink.media.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Core service for managing media assets.
 * Handles file processing, storage interactions, metadata extraction, and event publishing.
 */
@Service @RequiredArgsConstructor @Slf4j
public class MediaService
{
    private final StorageService                storageService;
    private final AssetRepository               assetRepository;
    private final FolderRepository              folderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${application.storage.s3.bucket}")
    private String                              bucket;
    @Value("${application.storage.s3.endpoint}")
    private String                              endpoint;        // Used to construct URL if needed
    /**
     * Uploads a new media asset, extracts metadata, persists to database, and publishes an event.
     *
     * @param file The multipart file to upload.
     * @param ownerId The UUID of the user uploading the file.
     * @param folderId Optional folder UUID to organize the asset.
     * @return The saved Asset entity.
     * @throws StorageException if the upload fails or I/O errors occur.
     */
    @Transactional
    public Asset uploadAsset( MultipartFile file, UUID ownerId, UUID folderId )
    {
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        log.info( LogConstants.LOG_UPLOAD_START, originalFilename, ownerId, contentType );
        // 1. Extract Metadata & Generate Thumbnail (if image)
        int width = 0;
        int height = 0;
        String thumbnailPath = null;
        boolean isImage = contentType != null && contentType.startsWith( AppConstants.PREFIX_IMAGE );
        if ( isImage )
        {
            log.debug( LogConstants.LOG_IMAGE_METADATA_PROCESSING, originalFilename );
            try (InputStream is = file.getInputStream())
            {
                BufferedImage image = ImageIO.read( is );
                if ( image != null )
                {
                    width = image.getWidth();
                    height = image.getHeight();
                    log.debug( LogConstants.LOG_IMAGE_DIMENSIONS, width, height );
                    // Generate Thumbnail
                    thumbnailPath = uploadThumbnail( image, originalFilename );
                }
            }
            catch ( Exception e )
            {
                log.warn( LogConstants.LOG_IMAGE_METADATA_FAILED, originalFilename, e );
            }
        }
        // 2. Upload Original File to Storage (MinIO/S3)
        // Structure: <random-uuid>/<original-filename> prevents collisions
        String key = UUID.randomUUID() + "/" + originalFilename;
        try
        {
            log.info( LogConstants.LOG_STORAGE_UPLOAD_START, key );
            storageService.upload( file, key );
        }
        catch ( IOException e )
        {
            log.error( LogConstants.LOG_STORAGE_UPLOAD_FAILED, key, e );
            throw new StorageException( ExceptionConstants.ERR_STORAGE_UPLOAD_FAILED, e );
        }
        String publicUrl = constructUrl( key );
        // 3. Save Entity to Database
        Asset asset = Asset.builder().ownerId( ownerId ).url( publicUrl ).filename( originalFilename )
                .mimeType( contentType ).sizeBytes( size ).width( width > 0 ? width : null )
                .height( height > 0 ? height : null ).thumbnailPath( thumbnailPath ).build();
        if ( folderId != null )
        {
            asset.setFolder( folderRepository.getReferenceById( folderId ) );
            log.debug( LogConstants.LOG_FOLDER_ASSOCIATION, folderId );
        }
        asset = assetRepository.save( asset );
        log.info( LogConstants.LOG_ASSET_SAVED, asset.getId() );
        // 4. Publish Event to Kafka with manual serialization
        MediaUploadedEvent event = new MediaUploadedEvent( asset.getId(), ownerId, publicUrl, contentType, size );
        try
        {
            String json = new ObjectMapper().writeValueAsString( event );
            ProducerRecord<String, String> record = new ProducerRecord<>( AppConstants.KAFKA_TOPIC_MEDIA_EVENTS,
                                                                          null,
                                                                          json );
            // Add Validation Header expected by listeners sharing the same DTO definition: __TypeId__
            record.headers().add( AppConstants.KAFKA_HEADER_TYPE_ID,
                                  AppConstants.SERIALIZATION_ID_MEDIA.getBytes( StandardCharsets.UTF_8 ) );
            kafkaTemplate.send( record );
            log.info( LogConstants.LOG_EVENT_PUBLISHED, asset.getId() );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_EVENT_PUBLISH_FAILED, asset.getId(), e );
        }
        return asset;
    }

    /**
     * Deletes an asset by its ID, removing it from both the database and object storage.
     * 
     * @param id The UUID of the asset to delete.
     * @throws AssetNotFoundException if not found.
     */
    @Transactional
    public void deleteAsset( UUID id )
    {
        log.info( LogConstants.LOG_ASSET_DELETING, id );
        Asset asset = assetRepository.findById( id )
                .orElseThrow( () -> new AssetNotFoundException( ExceptionConstants.ERR_ASSET_NOT_FOUND + id ) );
        // 1. Delete from Storage
        // Key extraction logic: URL is like endpoint/bucket/<key>
        try
        {
            String url = asset.getUrl();
            String key = null;
            if ( url.contains( bucket ) )
            {
                int bucketIndex = url.indexOf( bucket );
                int keyStartIndex = bucketIndex + bucket.length() + 1;
                if ( keyStartIndex < url.length() )
                {
                    key = url.substring( keyStartIndex );
                }
            }
            if ( key != null )
            {
                log.info( LogConstants.LOG_STORAGE_KEY_EXTRACTED, key );
                storageService.delete( key );
            }
            else
            {
                log.warn( LogConstants.LOG_STORAGE_KEY_EXTRACTION_FAILED, url );
            }
            // Delete Thumbnail if exists
            if ( asset.getThumbnailPath() != null )
            {
                log.info( LogConstants.LOG_THUMBNAIL_DELETING, asset.getThumbnailPath() );
                storageService.delete( asset.getThumbnailPath() );
            }
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_STORAGE_DELETE_FAILED, id, e );
        }
        // 2. Delete from DB
        assetRepository.delete( asset );
        log.info( LogConstants.LOG_ASSET_DELETED_DB, id );
    }

    /**
     * Helper method to generate and upload a thumbnail for an image.
     * Thumbnails are stored in a 'thumbnails/' prefix.
     * 
     * @param original The original BufferedImage.
     * @param filename The original filename to base the thumbnail name on.
     * @return The key of the uploaded thumbnail, or null if failed.
     */
    private String uploadThumbnail( BufferedImage original, String filename )
    {
        try
        {
            int targetWidth = 200;
            int targetHeight = 200;
            log.debug( LogConstants.LOG_THUMBNAIL_GENERATION, filename, targetWidth, targetHeight );
            // High-quality scaling using getScaledInstance
            Image tmp = original.getScaledInstance( targetWidth, targetHeight, Image.SCALE_SMOOTH );
            BufferedImage thumb = new BufferedImage( targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB );
            Graphics2D g = thumb.createGraphics();
            g.drawImage( tmp, 0, 0, null );
            g.dispose();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write( thumb, AppConstants.EXT_JPG, os );
            byte[] bytes = os.toByteArray();
            InputStream is = new ByteArrayInputStream( bytes );
            String thumbKey = AppConstants.PREFIX_THUMBNAILS + UUID.randomUUID() + "-" + filename + "."
                    + AppConstants.EXT_JPG;
            log.debug( LogConstants.LOG_THUMBNAIL_UPLOAD_START, thumbKey );
            storageService.upload( is, AppConstants.MIME_TYPE_JPEG, bytes.length, thumbKey );
            log.info( LogConstants.LOG_THUMBNAIL_UPLOAD_SUCCESS, thumbKey );
            return thumbKey;
        }
        catch ( Exception e )
        {
            // Log warning but do not stop the main flow
            log.warn( LogConstants.LOG_THUMBNAIL_GENERATION_FAILED, filename, e.getMessage() );
            return null;
        }
    }

    /**
     * Constructs the public URL for accessing the stored file.
     * 
     * @param key The storage key of the file.
     * @return The full public URL.
     */
    private String constructUrl( String key )
    {
        // for MinIO localhost: http://localhost:9000/bucket/key
        // In production, this logic might be replaced by a CDN URL generator
        return endpoint + "/" + bucket + "/" + key;
    }
}
