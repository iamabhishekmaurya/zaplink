package io.zaplink.media.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

/**
 * S3/MinIO implementation of StorageService.
 * Handles direct file operations (upload, delete) with the object storage provider.
 */
@Service @RequiredArgsConstructor @Slf4j
public class MinioStorageService
    implements
    StorageService
{
    private final S3Client s3Client;
    @Value("${application.storage.s3.bucket}")
    private String         bucketName;
    /**
     * Initializes the storage service.
     * Checks if the configured bucket exists; creates it if valid/authorized but missing.
     */
    @PostConstruct
    public void init()
    {
        try
        {
            s3Client.headBucket( HeadBucketRequest.builder().bucket( bucketName ).build() );
            log.info( "S3 Bucket '{}' exists and is accessible.", bucketName );
        }
        catch ( NoSuchBucketException e )
        {
            log.info( "Bucket '{}' does not exist. Attempting to create...", bucketName );
            try
            {
                s3Client.createBucket( b -> b.bucket( bucketName ) );
                log.info( "Bucket '{}' created successfully.", bucketName );
            }
            catch ( Exception createEx )
            {
                log.error( "Failed to create bucket '{}'. Ensure S3/MinIO credentials have create permissions.",
                           bucketName, createEx );
                throw createEx;
            }
        }
        catch ( Exception e )
        {
            log.error( "Error accessing S3 bucket '{}': {}", bucketName, e.getMessage() );
            // We might choose to fail startup here or handle gracefully depending on requirements
        }
    }

    /**
     * Uploads a MultipartFile to S3.
     * 
     * @param file The file to upload.
     * @param key The object key (path).
     * @return The key of the uploaded object.
     * @throws IOException If I/O error occurs.
     */
    @Override
    public String upload( MultipartFile file, String key )
        throws IOException
    {
        log.debug( "Delegating upload for file: {} to key: {}", file.getOriginalFilename(), key );
        return upload( file.getInputStream(), file.getContentType(), file.getSize(), key );
    }

    /**
     * Uploads a stream to S3.
     * 
     * @param inputStream Data stream.
     * @param contentType MIME type.
     * @param size Content-Length.
     * @param key Object key.
     * @return The key.
     */
    @Override
    public String upload( java.io.InputStream inputStream, String contentType, long size, String key )
    {
        log.debug( "Uploading stream to S3. Key: {}, Size: {}, Type: {}", key, size, contentType );
        try
        {
            PutObjectRequest putOb = PutObjectRequest.builder().bucket( bucketName ).key( key )
                    .contentType( contentType ).build();
            s3Client.putObject( putOb, RequestBody.fromInputStream( inputStream, size ) );
            log.info( "Successfully uploaded object to S3: {}", key );
            return key;
        }
        catch ( Exception e )
        {
            log.error( "S3 Upload failed for key: {}", key, e );
            throw new RuntimeException( "S3 Upload failed", e ); // Or a specific StorageException
        }
    }

    /**
     * Deletes an object from S3.
     * 
     * @param key The object key.
     */
    @Override
    public void delete( String key )
    {
        log.info( "Deleting object from S3: {}", key );
        try
        {
            DeleteObjectRequest deleteOb = DeleteObjectRequest.builder().bucket( bucketName ).key( key ).build();
            s3Client.deleteObject( deleteOb );
            log.debug( "Delete request sent for key: {}", key );
        }
        catch ( Exception e )
        {
            log.error( "Failed to delete object: {}", key, e );
            // S3 idempotent delete doesn't always throw on missing keys, but connection errors will appear here.
        }
    }
}
