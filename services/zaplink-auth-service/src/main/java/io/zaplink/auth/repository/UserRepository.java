package io.zaplink.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.auth.common.constants.DatabaseConstants;
import io.zaplink.auth.entity.User;

@Repository
public interface UserRepository
    extends
    JpaRepository<User, Long>
{
    Optional<User> findByEmail( String email );

    Optional<User> findByUsername( String username );

    Optional<User> findByVerificationToken( String token );

    Optional<User> findByResetToken( String token );

    boolean existsByEmail( String email );

    boolean existsByUsername( String username );

    @Modifying @Query(DatabaseConstants.QUERY_UPDATE_RESET_TOKEN)
    void updateResetToken( @Param(DatabaseConstants.PARAM_EMAIL) String email,
                           @Param(DatabaseConstants.PARAM_TOKEN) String token,
                           @Param(DatabaseConstants.PARAM_EXPIRY) java.time.LocalDateTime expiry );

    @Modifying @Query(DatabaseConstants.QUERY_CLEAR_RESET_TOKEN)
    void clearResetToken( @Param(DatabaseConstants.PARAM_EMAIL) String email );

    @Modifying @Query(DatabaseConstants.QUERY_VERIFY_USER)
    void verifyUser( @Param(DatabaseConstants.PARAM_USER_ID) Long userId );
}
