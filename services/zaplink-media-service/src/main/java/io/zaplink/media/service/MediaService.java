package io.zaplink.media.service;

import io.zaplink.media.entity.Asset;
import io.zaplink.media.event.MediaUploadedEvent;
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
    @Transactional
    public Asset uploadAsset( MultipartFile file, UUID ownerId, UUID folderId )
    {
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        // 1. Extract Metadata & Generate Thumbnail (if image)
        int width = 0;
        int height = 0;
        boolean isImage = contentType != null && contentType.startsWith( "image/" );
        if ( isImage )
        {
            try (InputStream is = file.getInputStream())
            {
                BufferedImage image = ImageIO.read( is );
                if ( image != null )
                {
                    width = image.getWidth();
                    height = image.getHeight();
                    // Generate Thumbnail
                    uploadThumbnail( image, originalFilename );
                }
            }
            catch ( Exception e )
            {
                log.warn( "Failed to process image metadata for file: {}", originalFilename, e );
                // check if we want to fail upload if thumbnail fails? probably not.
            }
        }
        // 2. Upload Original
        String key = UUID.randomUUID() + "/" + originalFilename;
        try
        {
            storageService.upload( file, key );
        }
        catch ( IOException e )
        {
            throw new io.zaplink.media.common.exception.StorageException( "Failed to upload file to storage", e );
        }
        String publicUrl = constructUrl( key );
        // 3. Save Entity
        Asset asset = Asset.builder().ownerId( ownerId ).url( publicUrl ).filename( originalFilename )
                .mimeType( contentType ).sizeBytes( size ).width( width > 0 ? width : null )
                .height( height > 0 ? height : null )
                // .folder(folder) // Fetch folder if present
                .build();
        if ( folderId != null )
        {
            // asset.setFolder(folderRepository.getReferenceById(folderId)); // Simple ref
        }
        asset = assetRepository.save( asset );
        // 4. Publish Event
        MediaUploadedEvent event = new MediaUploadedEvent( asset.getId(), ownerId, publicUrl, contentType, size );
        try
        {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString( event );
            org.apache.kafka.clients.producer.ProducerRecord<String, String> record = new org.apache.kafka.clients.producer.ProducerRecord<>( "media-events",
                                                                                                                                              null,
                                                                                                                                              json );
            record.headers().add( "__TypeId__", "mediaUploaded".getBytes( java.nio.charset.StandardCharsets.UTF_8 ) );
            kafkaTemplate.send( record );
        }
        catch ( Exception e )
        {
            log.error( "Failed to publish media event", e );
        }
        return asset;
    }

    private void uploadThumbnail( BufferedImage original, String filename )
    {
        try
        {
            int targetWidth = 200;
            int targetHeight = 200;
            // Basic resize
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
            storageService.upload( is, "image/jpeg", bytes.length, thumbKey );
        }
        catch ( Exception e )
        {
            log.warn( "Failed to generate thumbnail for {}", filename, e );
        }
    }

    private String constructUrl( String key )
    {
        // for MinIO localhost: http://localhost:9000/bucket/key
        return endpoint + "/" + bucket + "/" + key;
    }
}
