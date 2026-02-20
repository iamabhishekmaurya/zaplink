package io.zaplink.media.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

public interface StorageService
{
    String upload( MultipartFile file, String key )
        throws IOException;

    String upload( InputStream inputStream, String contentType, long size, String key );

    void delete( String key );

    InputStream download( String key );
}
