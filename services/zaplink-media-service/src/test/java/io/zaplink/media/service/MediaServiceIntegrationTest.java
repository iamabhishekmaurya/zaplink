package io.zaplink.media.service;

import io.zaplink.media.entity.MediaItem;
import io.zaplink.media.repository.MediaItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
    private MediaItemService    mediaItemService;
    @MockitoBean
    private S3Client            s3Client;
    @Autowired
    private MediaItemRepository mediaItemRepository;
    @BeforeEach
    void setUp()
    {
        mediaItemRepository.deleteAll();
    }

    @Test
    void testUploadAsset()
        throws IOException
    {
        // Arrange
        MockMultipartFile file = new MockMultipartFile( "file", "test-image.jpg", "image/jpeg", new byte[1024] );
        String ownerId = UUID.randomUUID().toString();
        // Act
        MediaItem item = mediaItemService.uploadMedia( file, null, ownerId, null );
        // Assert
        assertNotNull( item.getId() );
        assertEquals( "test-image.jpg", item.getName() );
        assertEquals( "image/jpeg", item.getType() );
        assertEquals( ownerId, item.getOwnerId() );
        // Verify S3 interaction
        verify( s3Client, atLeast( 1 ) ).putObject( any( PutObjectRequest.class ), any( RequestBody.class ) );
    }
}
