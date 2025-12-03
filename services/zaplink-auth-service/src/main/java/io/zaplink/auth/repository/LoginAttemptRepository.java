package io.zaplink.auth.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.auth.common.constants.DatabaseConstants;
import io.zaplink.auth.entity.LoginAttempt;

@Repository
public interface LoginAttemptRepository
    extends
    JpaRepository<LoginAttempt, Long>
{
    List<LoginAttempt> findByEmailOrderByCreatedAtDesc( String email );

    @Query(DatabaseConstants.QUERY_COUNT_FAILED_ATTEMPTS_SINCE)
    long countFailedAttemptsSince( @Param(DatabaseConstants.PARAM_EMAIL) String email,
                                   @Param(DatabaseConstants.PARAM_SINCE) LocalDateTime since );

    @Query(DatabaseConstants.QUERY_RECENT_FAILED_ATTEMPTS)
    List<LoginAttempt> findRecentFailedAttempts( @Param(DatabaseConstants.PARAM_SINCE) LocalDateTime since );
}
