package io.zaplink.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
        ShortnerRequest request = new ShortnerRequest();
        request.setOriginalUrl( "https://example.com" );
        request.setTraceId( "valid-trace-id" );
        ShortnerResponse response = new ShortnerResponse();
        response.setUrl( "http://short.url/abc" );
        response.setTraceId( "valid-trace-id" );
        when( urlShortnerService.shortUrl( any( ShortnerRequest.class ), any() ) ).thenReturn( response );
        mockMvc.perform( post( "/shortner/short/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.url" ).value( "http://short.url/abc" ) )
                .andExpect( jsonPath( "$.traceId" ).value( "valid-trace-id" ) );
    }

    @Test
    void shortUrl_InvalidUrl_Returns400()
        throws Exception
    {
        ShortnerRequest request = new ShortnerRequest();
        request.setOriginalUrl( "invalid-url" ); // Invalid format
        request.setTraceId( "trace-1" );
        mockMvc.perform( post( "/shortner/short/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isBadRequest() );
    }

    @Test
    void shortUrl_MissingOriginalUrl_Returns400()
        throws Exception
    {
        ShortnerRequest request = new ShortnerRequest();
        // Original URL is null/blank
        request.setTraceId( "trace-1" );
        mockMvc.perform( post( "/shortner/short/url" ).contentType( MediaType.APPLICATION_JSON )
                .content( objectMapper.writeValueAsString( request ) ) ).andExpect( status().isBadRequest() );
    }
}
