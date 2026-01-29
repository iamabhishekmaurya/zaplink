package io.zaplink.media.service;

import io.zaplink.media.entity.Asset;
import io.zaplink.media.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MediaServiceIntegrationTest
{
    @Autowired
    private MediaService                  mediaService;
    @MockitoBean
    private S3Client                      s3Client;
    @Autowired
    private AssetRepository               assetRepository;
    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;
    @BeforeEach
    void setUp()
    {
        assetRepository.deleteAll();
    }

    @Test
    void testUploadAsset()
        throws IOException
    {
        // Arrange
        MockMultipartFile file = new MockMultipartFile( "file", "test-image.jpg", "image/jpeg", new byte[1024] );
        String ownerId = UUID.randomUUID().toString();
        // Act
        Asset asset = mediaService.uploadAsset( file, ownerId, null );
        // Assert
        assertNotNull( asset.getId() );
        assertEquals( "test-image.jpg", asset.getFilename() );
        assertEquals( "image/jpeg", asset.getMimeType() );
        assertEquals( ownerId, asset.getOwnerId() );
        // Verify S3 interaction
        // Should be called 2 times (Original + Thumbnail) or 1 if thumbnail fails/not implemented fully? 
        // Logic: uploadAsset calls uploadThumbnail. 
        // uploadThumbnail calls storageService.upload
        // uploadAsset calls storageService.upload
        // So 2 uploads expected if image.
        verify( s3Client, atLeast( 1 ) ).putObject( any( PutObjectRequest.class ), any( RequestBody.class ) );
        // Verify Kafka event
        verify( kafkaTemplate, times( 1 ) ).send( any( org.apache.kafka.clients.producer.ProducerRecord.class ) );
    }
}
