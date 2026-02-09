package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.manager.common.constants.ControllerConstants;
import io.zaplink.manager.common.constants.StatusConstants;
import io.zaplink.manager.dto.response.LinkAnalyticsResponse;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.service.UrlManagerService;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping(ControllerConstants.SHORT_BASE_PATH) @Tag(name = ControllerConstants.TAG_SHORT_URL, description = ControllerConstants.TAG_SHORT_URL_DESC)
public class ShortUrlController
{
    private final UrlManagerService urlProvider;
    @GetMapping("/links") @Operation(summary = ControllerConstants.SHORT_GET_LINKS_SUMMARY, description = ControllerConstants.SHORT_GET_LINKS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_LINKS_RETRIEVED, content = @Content(schema = @Schema(implementation = LinkResponse.class))) })
    public List<LinkResponse> getLinks( @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlProvider.getLinksByUser( userEmail );
    }

    @GetMapping("/links/{id}") @Operation(summary = "Get short link by ID", description = "Retrieves a specific short link by its ID") @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = "Link retrieved successfully", content = @Content(schema = @Schema(implementation = LinkResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_LINK_NOT_FOUND) })
    public LinkResponse getLinkById( @Parameter(description = "Link ID") @PathVariable("id") String id,
                                   @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlProvider.getLinkById( id, userEmail );
    }

    @GetMapping("/stats") @Operation(summary = ControllerConstants.SHORT_GET_STATS_SUMMARY, description = ControllerConstants.SHORT_GET_STATS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_STATS_RETRIEVED, content = @Content(schema = @Schema(implementation = StatsResponse.class))) })
    public StatsResponse getStats( @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlProvider.getUserStats( userEmail );
    }

    @GetMapping("/{key}/analytics") @Operation(summary = ControllerConstants.SHORT_GET_ANALYTICS_SUMMARY, description = ControllerConstants.SHORT_GET_ANALYTICS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_LINK_ANALYTICS, content = @Content(schema = @Schema(implementation = LinkAnalyticsResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_LINK_NOT_FOUND) })
    public LinkAnalyticsResponse getLinkAnalytics( @Parameter(description = ControllerConstants.PARAM_LINK_KEY) @PathVariable("key") String key,
                                                   @RequestHeader(value = ControllerConstants.HEADER_USER_EMAIL, required = false) String userEmail )
    {
        return urlProvider.getLinkAnalytics( key, userEmail );
    }
}
