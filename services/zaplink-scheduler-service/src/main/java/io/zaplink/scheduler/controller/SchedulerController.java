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

import io.zaplink.scheduler.common.constants.ApiConstants;
import io.zaplink.scheduler.common.constants.LogMessages;
import io.zaplink.scheduler.entity.ScheduledPost;
import io.zaplink.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Scheduler Service.
 * <p>
 * Exposes endpoints for scheduling posts, retrieving scheduled posts, and updating schedules.
 * </p>
 */
@RestController @RequestMapping(ApiConstants.BASE_URI) @RequiredArgsConstructor @Slf4j
public class SchedulerController
{
    private final SchedulerService schedulerService;
    /**
     * Schedules a new post.
     *
     * @param post   The post details body.
     * @param userId The ID of the user request (from headers).
     * @return The created scheduled post entity.
     */
    @PostMapping
    public ResponseEntity<ScheduledPost> schedulePost( @RequestBody ScheduledPost post,
                                                       @RequestHeader(ApiConstants.HEADER_USER_ID) String userId )
    {
        log.info( LogMessages.API_SCHEDULE_POST, userId );
        post.setOwnerId( userId );
        return ResponseEntity.ok( schedulerService.schedulePost( post ) );
    }

    /**
     * Retrieves a list of posts scheduled between a start and end time.
     *
     * @param start  The start time (inclusive).
     * @param end    The end time (exclusive).
     * @param userId The ID of the user.
     * @return List of matching scheduled posts.
     */
    @GetMapping
    public ResponseEntity<List<ScheduledPost>> getPosts( @RequestParam(ApiConstants.PARAM_START) Instant start,
                                                         @RequestParam(ApiConstants.PARAM_END) Instant end,
                                                         @RequestHeader(ApiConstants.HEADER_USER_ID) String userId )
    {
        log.debug( LogMessages.API_GET_POSTS, userId, start, end );
        return ResponseEntity.ok( schedulerService.getPosts( userId, start, end ) );
    }

    /**
     * Updates the scheduled time of a post (e.g., via drag-and-drop UI).
     *
     * @param id      The UUID of the post to update.
     * @param request The request body containing new time.
     * @return The updated post entity.
     */
    @PatchMapping(ApiConstants.DRAG_UPDATE_URI)
    public ResponseEntity<ScheduledPost> updateScheduledTime( @PathVariable UUID id,
                                                              @RequestBody UpdateTimeRequest request )
    {
        log.info( LogMessages.API_UPDATE_TIME, id );
        return ResponseEntity.ok( schedulerService.updateScheduledTime( id, request.newTime() ) );
    }
    /**
     * DTO for update time requests.
     *
     * @param newTime The new instant to schedule the post.
     */
    public record UpdateTimeRequest( Instant newTime )
    {
    }
}
