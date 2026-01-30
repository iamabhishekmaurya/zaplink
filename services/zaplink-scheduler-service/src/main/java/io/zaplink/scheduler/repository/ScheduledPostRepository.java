package io.zaplink.scheduler.repository;

import io.zaplink.scheduler.entity.ScheduledPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledPostRepository
    extends
    JpaRepository<ScheduledPost, UUID>
{
    @Query("SELECT p FROM ScheduledPost p WHERE p.ownerId = :ownerId AND p.scheduledTime BETWEEN :start AND :end")
    List<ScheduledPost> findByOwnerAndDateRange( UUID ownerId, Instant start, Instant end );

    List<ScheduledPost> findByOwnerId( UUID ownerId );
}
