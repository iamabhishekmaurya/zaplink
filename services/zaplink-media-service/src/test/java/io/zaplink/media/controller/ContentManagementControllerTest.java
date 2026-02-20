package io.zaplink.media.controller;

import io.zaplink.media.entity.Folder;
import io.zaplink.media.service.FolderService;
import io.zaplink.media.service.MediaItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContentManagementControllerTest
{
    private MockMvc                     mockMvc;
    @Mock
    private FolderService               folderService;
    @Mock
    private MediaItemService            mediaItemService;
    @InjectMocks
    private ContentManagementController controller;
    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( controller ).build();
    }

    @Test
    void testCreateFolder()
        throws Exception
    {
        Folder mockFolder = Folder.builder().id( UUID.randomUUID() ).name( "Test Folder" ).ownerId( "user1" ).build();
        when( folderService.createFolder( eq( "Test Folder" ), any(), eq( "user1" ) ) ).thenReturn( mockFolder );
        mockMvc.perform( post( "/api/content/folders" ).header( "X-User-Id", "user1" ).param( "name", "Test Folder" )
                .contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name" ).value( "Test Folder" ) );
    }

    @Test
    void testListFolders()
        throws Exception
    {
        Folder mockFolder = Folder.builder().id( UUID.randomUUID() ).name( "Test Folder" ).ownerId( "user1" ).build();
        when( folderService.listFolders( "user1", null ) ).thenReturn( Collections.singletonList( mockFolder ) );
        mockMvc.perform( get( "/api/content/folders" ).header( "X-User-Id", "user1" )
                .contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$[0].name" ).value( "Test Folder" ) );
    }
}
