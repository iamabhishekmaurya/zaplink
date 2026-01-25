package io.zaplink.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.service.shortner.UrlShortnerService;

@WebMvcTest(CoreController.class)
class ZaplinkCoreControllerTest
{
    @Autowired
    private MockMvc            mockMvc;
    @MockitoBean
    private UrlShortnerService urlShortnerService;
    @Autowired
    private ObjectMapper       objectMapper;
    @Test
    void shortUrl_ValidRequest_Returns200()
        throws Exception
    {
        ShortnerRequest request = new ShortnerRequest( "https://example.com",
                                                       "Title",
                                                       "Platform",
                                                       null,
                                                       "valid-trace-id" );
        ShortnerResponse response = new ShortnerResponse( "http://short.url/abc", "valid-trace-id" );
        when( urlShortnerService.createShortUrl( any( ShortnerRequest.class ), any() ) ).thenReturn( response );
        mockMvc.perform( post( "/core/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.url" ).value( "http://short.url/abc" ) )
                .andExpect( jsonPath( "$.traceId" ).value( "valid-trace-id" ) );
    }

    @Test
    void shortUrl_InvalidUrl_Returns400()
        throws Exception
    {
        ShortnerRequest request = new ShortnerRequest( "invalid-url", null, null, null, "trace-1" );
        mockMvc.perform( post( "/core/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isBadRequest() );
    }

    @Test
    void shortUrl_MissingOriginalUrl_Returns400()
        throws Exception
    {
        ShortnerRequest request = new ShortnerRequest( null, null, null, null, "trace-1" );
        // Original URL is null/blank
        mockMvc.perform( post( "/core/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isBadRequest() );
    }
}
