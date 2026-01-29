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

@Service @RequiredArgsConstructor @Slf4j
public class MinioStorageService
    implements
    StorageService
{
    private final S3Client s3Client;
    @Value("${application.storage.s3.bucket}")
    private String         bucketName;
    @PostConstruct
    public void init()
    {
        try
        {
            s3Client.headBucket( HeadBucketRequest.builder().bucket( bucketName ).build() );
            log.info( "Bucket '{}' exists.", bucketName );
        }
        catch ( NoSuchBucketException e )
        {
            log.info( "Bucket '{}' does not exist. Creating...", bucketName );
            s3Client.createBucket( b -> b.bucket( bucketName ) );
        }
    }

    @Override
    public String upload( MultipartFile file, String key )
        throws IOException
    {
        return upload( file.getInputStream(), file.getContentType(), file.getSize(), key );
    }

    @Override
    public String upload( java.io.InputStream inputStream, String contentType, long size, String key )
    {
        PutObjectRequest putOb = PutObjectRequest.builder().bucket( bucketName ).key( key ).contentType( contentType )
                .build();
        s3Client.putObject( putOb, RequestBody.fromInputStream( inputStream, size ) );
        return key;
    }

    @Override
    public void delete( String key )
    {
        DeleteObjectRequest deleteOb = DeleteObjectRequest.builder().bucket( bucketName ).key( key ).build();
        s3Client.deleteObject( deleteOb );
    }
}
