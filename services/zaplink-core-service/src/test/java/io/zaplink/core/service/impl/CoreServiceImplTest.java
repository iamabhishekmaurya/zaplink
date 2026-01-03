package io.zaplink.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.entity.UrlMappingEntity;
import io.zaplink.core.repository.UrlMappingRepository;
import io.zaplink.core.service.shortner.impl.UrlShortnerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CoreServiceImplTest
{
    @Mock
    private UrlMappingRepository   urlMappingRepository;
    @InjectMocks
    private UrlShortnerServiceImpl urlShortnerService;
    private static final String    BASE_URL = "http://localhost:8083/";
    @BeforeEach
    void setUp()
    {
        ReflectionTestUtils.setField( urlShortnerService, "BASE_URL", BASE_URL );
    }

    @Test
    void shortUrl_Success()
    {
        // Arrange
        String originalUrl = "https://www.example.com";
        String traceId = "test-trace-id";
        ShortnerRequest request = new ShortnerRequest();
        request.setOriginalUrl( originalUrl );
        request.setTraceId( traceId );
        UrlMappingEntity savedEntity = new UrlMappingEntity();
        savedEntity.setShortUrl( "http://localhost:8083/1" );
        savedEntity.setTraceId( traceId );
        when( urlMappingRepository.save( any( UrlMappingEntity.class ) ) ).thenReturn( savedEntity );
        ShortnerResponse response = urlShortnerService.shortUrl( request, "test@example.com" );
        // Assert
        assertNotNull( response );
        assertEquals( savedEntity.getShortUrl(), response.getUrl() );
        assertEquals( traceId, response.getTraceId() );
        verify( urlMappingRepository ).save( any( UrlMappingEntity.class ) );
    }

    @Test
    void shortUrl_RepositorySaveReturnsNull_CalculatedValuesReturnedCurrently()
    {
        // Note: The current implementation returns the calculated shortUrl if repository returns null,
        // (impl checks savedUrlMappingEntity != null block but returns 'shortUrlResponse' anyway)
        // This test documents current behavior, though ideally we might want to throw an error.
        // Arrange
        String originalUrl = "https://www.google.com";
        ShortnerRequest request = new ShortnerRequest();
        request.setOriginalUrl( originalUrl );
        request.setTraceId( "trace-123" );
        when( urlMappingRepository.save( any( UrlMappingEntity.class ) ) ).thenReturn( null );
        ShortnerResponse response = urlShortnerService.shortUrl( request, "test@example.com" );
        // Assert
        assertNotNull( response );
        // Current impl: if save returns null, response object uses default initialized values (null url)
        // logic: ShortnerResponse shortUrlResponse = new ShortnerResponse(); ... if (saved != null) { setUrl... } return shortUrlResponse.
        assertEquals( null, response.getUrl() );
    }

    @Test
    void shortUrl_RepositoryThrowsException_PropagatesException()
    {
        // Arrange
        ShortnerRequest request = new ShortnerRequest();
        request.setOriginalUrl( "https://error.com" );
        when( urlMappingRepository.save( any( UrlMappingEntity.class ) ) )
                .thenThrow( new RuntimeException( "DB Error" ) );
        // Act & Assert
        assertThrows( RuntimeException.class, () -> urlShortnerService.shortUrl( request, "test@example.com" ) );
    }
}
