package io.zaplink.media.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService
{
    String upload( MultipartFile file, String key )
        throws IOException;

    String upload( java.io.InputStream inputStream, String contentType, long size, String key );

    void delete( String key );
}
