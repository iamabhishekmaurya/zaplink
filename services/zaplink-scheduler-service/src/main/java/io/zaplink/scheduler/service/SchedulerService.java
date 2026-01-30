package io.zaplink.scheduler.service;

import io.zaplink.scheduler.common.constants.Constants;
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

@Service @RequiredArgsConstructor @Slf4j
public class SchedulerService
{
    private final ScheduledPostRepository scheduledPostRepository;
    private final Scheduler               scheduler;
    @Transactional
    public ScheduledPost schedulePost( ScheduledPost post )
    {
        // validate post (omitted for brevity)
        if ( post.getScheduledTime().isBefore( Instant.now() ) )
        {
            throw new IllegalArgumentException( "Cannot schedule post in the past" );
        }
        post.setStatus( ScheduledPost.PostStatus.SCHEDULED );
        ScheduledPost savedPost = scheduledPostRepository.save( post );
        scheduleQuartzJob( savedPost );
        log.info( Constants.LOG_SCHEDULING_POST, savedPost.getId(), savedPost.getScheduledTime() );
        return savedPost;
    }

    public List<ScheduledPost> getPosts( UUID ownerId, Instant start, Instant end )
    {
        return scheduledPostRepository.findByOwnerAndDateRange( ownerId, start, end );
    }

    @Transactional
    public ScheduledPost updateScheduledTime( UUID postId, Instant newTime )
    {
        ScheduledPost post = scheduledPostRepository.findById( postId )
                .orElseThrow( () -> new IllegalArgumentException( Constants.EXCEPTION_POST_NOT_FOUND + postId ) );
        if ( post.getStatus() == ScheduledPost.PostStatus.PUBLISHED )
        {
            throw new IllegalArgumentException( "Cannot reschedule a published post" );
        }
        if ( newTime.isBefore( Instant.now() ) )
        {
            throw new IllegalArgumentException( "Cannot schedule post in the past" );
        }
        post.setScheduledTime( newTime );
        post.setStatus( ScheduledPost.PostStatus.SCHEDULED ); // Reset status if it was FAILED or DRAFT
        ScheduledPost savedPost = scheduledPostRepository.save( post );
        // Reschedule Quartz Job
        rescheduleQuartzJob( savedPost );
        log.info( Constants.LOG_RESCHEDULING_POST, postId, newTime );
        return savedPost;
    }

    private void scheduleQuartzJob( ScheduledPost post )
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob( PostPublishJob.class )
                    .withIdentity( post.getId().toString(), Constants.JOB_GROUP_NAME )
                    .usingJobData( "postId", post.getId().toString() ).storeDurably().build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity( post.getId().toString(), Constants.JOB_GROUP_NAME )
                    .startAt( Date.from( post.getScheduledTime() ) )
                    .withSchedule( SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow() )
                    .build();
            scheduler.scheduleJob( jobDetail, trigger );
        }
        catch ( SchedulerException e )
        {
            log.error( "Failed to schedule quartz job for post {}", post.getId(), e );
            throw new RuntimeException( "Failed to schedule post", e );
        }
    }

    private void rescheduleQuartzJob( ScheduledPost post )
    {
        try
        {
            TriggerKey triggerKey = TriggerKey.triggerKey( post.getId().toString(), Constants.JOB_GROUP_NAME );
            // If trigger exists, reschedule it
            if ( scheduler.checkExists( triggerKey ) )
            {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .withIdentity( post.getId().toString(), Constants.JOB_GROUP_NAME )
                        .startAt( Date.from( post.getScheduledTime() ) )
                        .withSchedule( SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow() )
                        .build();
                scheduler.rescheduleJob( triggerKey, newTrigger );
            }
            else
            {
                // If it doesn't exist (maybe was manually deleted or not scheduled yet), schedule it fresh
                scheduleQuartzJob( post );
            }
        }
        catch ( SchedulerException e )
        {
            log.error( "Failed to reschedule quartz job for post {}", post.getId(), e );
            throw new RuntimeException( "Failed to reschedule post", e );
        }
    }
}
