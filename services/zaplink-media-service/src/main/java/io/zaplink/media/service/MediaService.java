package io.zaplink.media.service;

import io.zaplink.media.dto.event.MediaUploadedEvent;
import io.zaplink.media.entity.Asset;
import io.zaplink.media.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Core service for managing media assets.
 * Handles file processing, storage interactions, metadata extraction, and event publishing.
 */
@Service @RequiredArgsConstructor @Slf4j
public class MediaService
{
    private final StorageService                storageService;
    private final AssetRepository               assetRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${application.storage.s3.bucket}")
    private String                              bucket;
    @Value("${application.storage.s3.endpoint}")
    private String                              endpoint;       // Used to construct URL if needed
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
        log.info( "Starting upload for file: {}, owner: {}, content-type: {}", originalFilename, ownerId, contentType );
        // 1. Extract Metadata & Generate Thumbnail (if image)
        int width = 0;
        int height = 0;
        boolean isImage = contentType != null && contentType.startsWith( "image/" );
        if ( isImage )
        {
            log.debug( "Processing image metadata for: {}", originalFilename );
            try (InputStream is = file.getInputStream())
            {
                BufferedImage image = ImageIO.read( is );
                if ( image != null )
                {
                    width = image.getWidth();
                    height = image.getHeight();
                    log.debug( "Image dimensions: {}x{}", width, height );
                    // Generate Thumbnail
                    uploadThumbnail( image, originalFilename );
                }
            }
            catch ( Exception e )
            {
                log.warn( "Failed to process image metadata/thumbnail for file: {}. Proceeding with original upload.",
                          originalFilename, e );
            }
        }
        // 2. Upload Original File to Storage (MinIO/S3)
        // Structure: <random-uuid>/<original-filename> prevents collisions
        String key = UUID.randomUUID() + "/" + originalFilename;
        try
        {
            log.info( "Uploading file to storage with key: {}", key );
            storageService.upload( file, key );
        }
        catch ( IOException e )
        {
            log.error( "Storage upload failed for key: {}", key, e );
            throw new io.zaplink.media.common.exception.StorageException( "Failed to upload file to storage", e );
        }
        String publicUrl = constructUrl( key );
        // 3. Save Entity to Database
        Asset asset = Asset.builder().ownerId( ownerId ).url( publicUrl ).filename( originalFilename )
                .mimeType( contentType ).sizeBytes( size ).width( width > 0 ? width : null )
                .height( height > 0 ? height : null )
                // .folder(folder) // Fetch folder if present
                .build();
        if ( folderId != null )
        {
            // asset.setFolder(folderRepository.getReferenceById(folderId)); // Simple ref
            log.debug( "Associating asset with folder: {}", folderId );
        }
        asset = assetRepository.save( asset );
        log.info( "Asset saved to database. ID: {}", asset.getId() );
        // 4. Publish Event to Kafka with manual serialization
        MediaUploadedEvent event = new MediaUploadedEvent( asset.getId(), ownerId, publicUrl, contentType, size );
        try
        {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString( event );
            org.apache.kafka.clients.producer.ProducerRecord<String, String> record = new org.apache.kafka.clients.producer.ProducerRecord<>( "media-events",
                                                                                                                                              null,
                                                                                                                                              json );
            // Add Validation Header expected by listeners sharing the same DTO definition: __TypeId__
            record.headers().add( "__TypeId__", "mediaUploaded".getBytes( java.nio.charset.StandardCharsets.UTF_8 ) );
            kafkaTemplate.send( record );
            log.info( "Published media-events for asset: {}", asset.getId() );
        }
        catch ( Exception e )
        {
            log.error( "Failed to publish media event for asset: {}", asset.getId(), e );
        }
        return asset;
    }

    /**
     * Helper method to generate and upload a thumbnail for an image.
     * Thumbnails are stored in a 'thumbnails/' prefix.
     * 
     * @param original The original BufferedImage.
     * @param filename The original filename to base the thumbnail name on.
     */
    private void uploadThumbnail( BufferedImage original, String filename )
    {
        try
        {
            int targetWidth = 200;
            int targetHeight = 200;
            log.debug( "Generating thumbnail for {}. Target size: {}x{}", filename, targetWidth, targetHeight );
            // High-quality scaling using getScaledInstance
            Image tmp = original.getScaledInstance( targetWidth, targetHeight, Image.SCALE_SMOOTH );
            BufferedImage thumb = new BufferedImage( targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB );
            Graphics2D g = thumb.createGraphics();
            g.drawImage( tmp, 0, 0, null );
            g.dispose();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write( thumb, "jpg", os );
            byte[] bytes = os.toByteArray();
            InputStream is = new ByteArrayInputStream( bytes );
            String thumbKey = "thumbnails/" + UUID.randomUUID() + "-" + filename + ".jpg";
            log.debug( "Uploading thumbnail to storage key: {}", thumbKey );
            storageService.upload( is, "image/jpeg", bytes.length, thumbKey );
            log.info( "Thumbnail uploaded successfully: {}", thumbKey );
        }
        catch ( Exception e )
        {
            // Log warning but do not stop the main flow
            log.warn( "Failed to generate thumbnail for {}: {}", filename, e.getMessage() );
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
