package io.zaplink.auth.service.helper;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.common.util.StringUtil;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common user operations.
 * Provides reusable methods for user validation and management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @Component @RequiredArgsConstructor
public class UserHelper
{
    private final UserRepository userRepository;
    /**
     * Finds a user by ID and throws exception if not found.
     * 
     * @param userId The user ID to search for
     * @param operation The operation name for logging
     * @return The found user
     * @throws UserNotFoundException if user is not found
     */
    public User findUserByIdOrThrow( Long userId, String operation )
    {
        return userRepository.findById( userId ).orElseThrow( () -> {
            log.error( LogConstants.LOG_USER_NOT_FOUND_FOR, operation, userId );
            return new UserNotFoundException( StringUtil.appendValue( ApiConstants.MESSAGE_USER_NOT_FOUND_WITH_ID,
                                                                      userId ) );
        } );
    }

    /**
     * Finds a user by email and throws exception if not found.
     * 
     * @param email The user email to search for
     * @param operation The operation name for logging
     * @return The found user
     * @throws UserNotFoundException if user is not found
     */
    public User findUserByEmailOrThrow( String email, String operation )
    {
        return userRepository.findByEmail( email ).orElseThrow( () -> {
            log.error( LogConstants.LOG_USER_NOT_FOUND_FOR, operation, email );
            return new UserNotFoundException( StringUtil.appendValue( ApiConstants.MESSAGE_USER_NOT_FOUND_WITH_EMAIL,
                                                                      email ) );
        } );
    }

    /**
     * Updates a user with the provided changes and saves to database.
     * 
     * @param user The user to update
     * @param updater Function to apply updates to the user
     * @return The updated user
     */
    public User updateUser( User user, Consumer<User> updater )
    {
        updater.accept( user );
        return userRepository.save( user );
    }

    /**
     * Validates that a user account is active.
     * 
     * @param user The user to validate
     * @param operation The operation being performed
     * @throws IllegalArgumentException if user is not active
     */
    public void validateUserActive( User user, String operation )
    {
        if ( !user.isActive() )
        {
            log.warn( LogConstants.LOG_ATTEMPTED_FOR_DEACTIVATED_USER, operation, user.getEmail() );
            throw new IllegalArgumentException( ApiConstants.MESSAGE_USER_ACCOUNT_DEACTIVATED );
        }
    }
}
