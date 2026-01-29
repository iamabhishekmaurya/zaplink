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

import io.zaplink.media.common.constants.ExceptionConstants;
import io.zaplink.media.common.constants.LogConstants;
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
            log.info( LogConstants.LOG_BUCKET_EXISTS, bucketName );
            setPublicBucketPolicy( bucketName );
        }
        catch ( NoSuchBucketException e )
        {
            log.info( LogConstants.LOG_BUCKET_NOT_FOUND, bucketName );
            try
            {
                s3Client.createBucket( b -> b.bucket( bucketName ) );
                log.info( LogConstants.LOG_BUCKET_CREATED, bucketName );
                setPublicBucketPolicy( bucketName );
            }
            catch ( Exception createEx )
            {
                log.error( LogConstants.LOG_BUCKET_CREATION_FAILED, bucketName, createEx );
                throw createEx;
            }
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_BUCKET_ACCESS_ERROR, bucketName, e.getMessage() );
            // We might choose to fail startup here or handle gracefully depending on requirements
        }
    }

    private void setPublicBucketPolicy( String bucketName )
    {
        try
        {
            String policy = String
                    .format( "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
                             bucketName );
            s3Client.putBucketPolicy( software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest.builder()
                    .bucket( bucketName ).policy( policy ).build() );
            log.info( "Public read policy set for bucket: {}", bucketName );
        }
        catch ( Exception e )
        {
            log.error( "Failed to set bucket policy for {}", bucketName, e );
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
        log.debug( LogConstants.LOG_UPLOAD_DELEGATION, file.getOriginalFilename(), key );
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
        log.debug( LogConstants.LOG_STREAM_UPLOAD_START, key, size, contentType );
        try
        {
            PutObjectRequest putOb = PutObjectRequest.builder().bucket( bucketName ).key( key )
                    .contentType( contentType ).build();
            s3Client.putObject( putOb, RequestBody.fromInputStream( inputStream, size ) );
            log.info( LogConstants.LOG_S3_UPLOAD_SUCCESS, key );
            return key;
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_S3_UPLOAD_FAILED, key, e );
            throw new RuntimeException( ExceptionConstants.ERR_S3_UPLOAD_FAILED, e ); // Or a specific StorageException
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
        log.info( LogConstants.LOG_S3_DELETE_START, key );
        try
        {
            DeleteObjectRequest deleteOb = DeleteObjectRequest.builder().bucket( bucketName ).key( key ).build();
            s3Client.deleteObject( deleteOb );
            log.debug( LogConstants.LOG_S3_DELETE_SENT, key );
        }
        catch ( Exception e )
        {
            log.error( LogConstants.LOG_S3_DELETE_FAILED, key, e );
            // S3 idempotent delete doesn't always throw on missing keys, but connection errors will appear here.
        }
    }
}
