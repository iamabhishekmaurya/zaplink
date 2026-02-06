package io.zaplink.scheduler.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.scheduler.common.constants.ApiConstants;
import io.zaplink.scheduler.common.constants.ControllerConstants;
import io.zaplink.scheduler.common.constants.LogMessages;
import io.zaplink.scheduler.common.constants.StatusConstants;
import io.zaplink.scheduler.dto.request.SchedulePostRequest;
import io.zaplink.scheduler.dto.request.UpdateTimeRequest;
import io.zaplink.scheduler.dto.response.ScheduledPostResponse;
import io.zaplink.scheduler.entity.ScheduledPost;
import io.zaplink.scheduler.service.SchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Scheduler Service.
 * <p>
 * Exposes endpoints for scheduling posts, retrieving scheduled posts, and updating schedules.
 * </p>
 */
@Slf4j @RestController @RequestMapping(ApiConstants.BASE_URI) @RequiredArgsConstructor @Tag(name = ControllerConstants.TAG_SCHEDULER, description = ControllerConstants.TAG_SCHEDULER_DESC)
public class SchedulerController
{
    private final SchedulerService schedulerService;
    /**
     * Schedules a new post.
     */
    @PostMapping @Operation(summary = ControllerConstants.SCHEDULER_CREATE_SUMMARY, description = ControllerConstants.SCHEDULER_CREATE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_POST_SCHEDULED, content = @Content(schema = @Schema(implementation = ScheduledPostResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_DATA) })
    public ResponseEntity<ScheduledPostResponse> schedulePost( @Valid @RequestBody SchedulePostRequest request,
                                                               @RequestHeader(ApiConstants.HEADER_USER_ID) String userId )
    {
        log.info( LogMessages.API_SCHEDULE_POST, userId );
        ScheduledPost post = ScheduledPost.builder().caption( request.caption() )
                .mediaAssetIds( request.mediaAssetIds() ).scheduledTime( request.scheduledTime() )
                .socialAccountIds( request.socialAccountIds() ).ownerId( userId ).build();
        ScheduledPost savedPost = schedulerService.schedulePost( post );
        return ResponseEntity.ok( ScheduledPostResponse.from( savedPost ) );
    }

    /**
     * Retrieves a list of posts scheduled between a start and end time.
     */
    @GetMapping @Operation(summary = ControllerConstants.SCHEDULER_GET_POSTS_SUMMARY, description = ControllerConstants.SCHEDULER_GET_POSTS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_POSTS_RETRIEVED) })
    public ResponseEntity<List<ScheduledPostResponse>> getPosts( @Parameter(description = ControllerConstants.PARAM_START_TIME) @RequestParam(ApiConstants.PARAM_START) Instant start,
                                                                 @Parameter(description = ControllerConstants.PARAM_END_TIME) @RequestParam(ApiConstants.PARAM_END) Instant end,
                                                                 @RequestHeader(ApiConstants.HEADER_USER_ID) String userId )
    {
        log.debug( LogMessages.API_GET_POSTS, userId, start, end );
        List<ScheduledPostResponse> posts = schedulerService.getPosts( userId, start, end ).stream()
                .map( ScheduledPostResponse::from ).toList();
        return ResponseEntity.ok( posts );
    }

    /**
     * Updates the scheduled time of a post (e.g., via drag-and-drop UI).
     */
    @PatchMapping(ApiConstants.DRAG_UPDATE_URI) @Operation(summary = ControllerConstants.SCHEDULER_UPDATE_TIME_SUMMARY, description = ControllerConstants.SCHEDULER_UPDATE_TIME_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_TIME_UPDATED, content = @Content(schema = @Schema(implementation = ScheduledPostResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_PAST_TIME),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_POST_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_ALREADY_PUBLISHED) })
    public ResponseEntity<ScheduledPostResponse> updateScheduledTime( @Parameter(description = ControllerConstants.PARAM_POST_ID) @PathVariable UUID id,
                                                                      @Valid @RequestBody UpdateTimeRequest request )
    {
        log.info( LogMessages.API_UPDATE_TIME, id );
        ScheduledPost updatedPost = schedulerService.updateScheduledTime( id, request.newTime() );
        return ResponseEntity.ok( ScheduledPostResponse.from( updatedPost ) );
    }
}
