package io.zaplink.scheduler.service;

import io.zaplink.scheduler.common.constants.DbIdentifiers;
import io.zaplink.scheduler.common.constants.ErrorMessages;
import io.zaplink.scheduler.common.constants.LogMessages;
import io.zaplink.scheduler.entity.ScheduledPost;
import io.zaplink.scheduler.repository.ScheduledPostRepository;
import io.zaplink.scheduler.service.job.PostPublishJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing scheduled posts.
 * <p>
 * Handles the creation, retrieval, and rescheduling of social media posts.
 * Integrates with Quartz Scheduler to trigger post publication at specified times.
 * </p>
 */
@Service @RequiredArgsConstructor @Slf4j
public class SchedulerService
{
    private final ScheduledPostRepository scheduledPostRepository;
    private final Scheduler               scheduler;
    /**
     * Schedules a new post for publication.
     *
     * @param post The post entity to schedule.
     * @return The saved and scheduled post entity.
     * @throws IllegalArgumentException if the scheduled time is in the past.
     */
    @Transactional
    public ScheduledPost schedulePost( ScheduledPost post )
    {
        log.debug( LogMessages.ENTERING_SCHEDULE_POST, post );
        // 1. Validate Schedule Time
        if ( post.getScheduledTime().isBefore( Instant.now() ) )
        {
            log.warn( LogMessages.VALIDATION_FAILED_PAST, post.getScheduledTime() );
            throw new IllegalArgumentException( ErrorMessages.CANNOT_SCHEDULE_IN_PAST );
        }
        // 2. Save to Database
        post.setStatus( ScheduledPost.PostStatus.SCHEDULED );
        ScheduledPost savedPost = scheduledPostRepository.save( post );
        // 3. Schedule Quartz Job
        scheduleQuartzJob( savedPost );
        log.info( LogMessages.SCHEDULING_POST, savedPost.getId(), savedPost.getScheduledTime() );
        return savedPost;
    }

    /**
     * Retrieves scheduled posts for a specific owner within a time range.
     *
     * @param ownerId The UUID of the post owner.
     * @param start   The start of the time range (inclusive).
     * @param end     The end of the time range (exclusive).
     * @return A list of scheduled posts matching the criteria.
     */
    public List<ScheduledPost> getPosts( UUID ownerId, Instant start, Instant end )
    {
        log.debug( LogMessages.RETRIEVING_POSTS, ownerId, start, end );
        return scheduledPostRepository.findByOwnerAndDateRange( ownerId, start, end );
    }

    /**
     * Updates the scheduled time of an existing post.
     *
     * @param postId  The UUID of the post to update.
     * @param newTime The new time to schedule the post.
     * @return The updated post entity.
     * @throws IllegalArgumentException if the post is not found, is already published, or the new time is in the past.
     */
    @Transactional
    public ScheduledPost updateScheduledTime( UUID postId, Instant newTime )
    {
        log.debug( LogMessages.ENTERING_UPDATE_TIME, postId, newTime );
        // 1. Retrieve Post
        ScheduledPost post = scheduledPostRepository.findById( postId ).orElseThrow( () -> {
            log.error( LogMessages.POST_NOT_FOUND_UPDATE, postId );
            return new IllegalArgumentException( ErrorMessages.POST_NOT_FOUND + postId );
        } );
        // 2. Validate Status
        if ( post.getStatus() == ScheduledPost.PostStatus.PUBLISHED )
        {
            log.warn( LogMessages.RESCHEDULE_PUBLISHED, postId );
            throw new IllegalArgumentException( ErrorMessages.CANNOT_RESCHEDULE_PUBLISHED );
        }
        // 3. Validate New Time
        if ( newTime.isBefore( Instant.now() ) )
        {
            log.warn( LogMessages.RESCHEDULE_PAST_TIME, newTime );
            throw new IllegalArgumentException( ErrorMessages.CANNOT_SCHEDULE_IN_PAST );
        }
        // 4. Update and Save
        post.setScheduledTime( newTime );
        post.setStatus( ScheduledPost.PostStatus.SCHEDULED ); // Reset status if it was FAILED or DRAFT
        ScheduledPost savedPost = scheduledPostRepository.save( post );
        // 5. Reschedule Quartz Job
        rescheduleQuartzJob( savedPost );
        log.info( LogMessages.RESCHEDULING_POST, postId, newTime );
        return savedPost;
    }

    /**
     * Schedules a Quartz job for the given post.
     *
     * @param post The post to schedule a job for.
     */
    private void scheduleQuartzJob( ScheduledPost post )
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob( PostPublishJob.class )
                    .withIdentity( post.getId().toString(), DbIdentifiers.JOB_GROUP_NAME )
                    .usingJobData( DbIdentifiers.JOB_DATA_POST_ID, post.getId().toString() ).storeDurably().build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity( post.getId().toString(), DbIdentifiers.JOB_GROUP_NAME )
                    .startAt( Date.from( post.getScheduledTime() ) )
                    .withSchedule( SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow() )
                    .build();
            scheduler.scheduleJob( jobDetail, trigger );
            log.debug( LogMessages.QUARTZ_JOB_SCHEDULED, post.getId() );
        }
        catch ( SchedulerException e )
        {
            log.error( LogMessages.ERR_SCHEDULING_QUARTZ, post.getId(), e );
            throw new RuntimeException( ErrorMessages.FAILED_TO_SCHEDULE, e );
        }
    }

    /**
     * Reschedules an existing Quartz job.
     *
     * @param post The post to reschedule the job for.
     */
    private void rescheduleQuartzJob( ScheduledPost post )
    {
        try
        {
            TriggerKey triggerKey = TriggerKey.triggerKey( post.getId().toString(), DbIdentifiers.JOB_GROUP_NAME );
            if ( scheduler.checkExists( triggerKey ) )
            {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .withIdentity( post.getId().toString(), DbIdentifiers.JOB_GROUP_NAME )
                        .startAt( Date.from( post.getScheduledTime() ) )
                        .withSchedule( SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow() )
                        .build();
                scheduler.rescheduleJob( triggerKey, newTrigger );
                log.debug( LogMessages.QUARTZ_JOB_RESCHEDULED, post.getId() );
            }
            else
            {
                // If it doesn't exist (maybe manually deleted), schedule fresh
                log.warn( LogMessages.TRIGGER_NOT_FOUND, post.getId() );
                scheduleQuartzJob( post );
            }
        }
        catch ( SchedulerException e )
        {
            log.error( LogMessages.ERR_RESCHEDULING_QUARTZ, post.getId(), e );
            throw new RuntimeException( ErrorMessages.FAILED_TO_RESCHEDULE, e );
        }
    }
}
