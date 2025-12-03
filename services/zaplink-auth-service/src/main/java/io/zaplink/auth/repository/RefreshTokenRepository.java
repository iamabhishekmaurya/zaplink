package io.zaplink.auth.repository;

import io.zaplink.auth.common.constants.DatabaseConstants;
import io.zaplink.auth.entity.RefreshToken;
import io.zaplink.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository
    extends
    JpaRepository<RefreshToken, Long>
{
    Optional<RefreshToken> findByToken( String token );

    List<RefreshToken> findByUser( User user );

    List<RefreshToken> findByExpiryDateBefore( LocalDateTime date );

    @Modifying @Query(DatabaseConstants.QUERY_DELETE_EXPIRED_TOKENS)
    void deleteExpiredTokens( @Param(DatabaseConstants.PARAM_DATE) LocalDateTime date );

    @Modifying @Query(DatabaseConstants.QUERY_DELETE_BY_USER)
    void deleteByUser( @Param(DatabaseConstants.PARAM_USER) User user );
}
