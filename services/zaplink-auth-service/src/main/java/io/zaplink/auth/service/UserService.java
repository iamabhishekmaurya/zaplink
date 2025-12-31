package io.zaplink.auth.service;

import java.util.Optional;

import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.entity.User;

public interface UserService
{
    Optional<User> findByEmail( String email );

    Optional<User> findByUsername( String username );

    Optional<User> findById( Long id );

    User createUser( UserRegistrationRequest request );

    User saveUser( User user );

    boolean existsByEmail( String email );

    boolean existsByUsername( String username );

    void updateUser( Long userId, User updatedUser );

    void deactivateUser( Long userId );

    void activateUser( Long userId );
}
