package io.zaplink.auth.service.impl;

import java.time.Instant;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.exception.UserAlreadyExistsException;
import io.zaplink.auth.common.util.Utility;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.entity.Role;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.RoleRepository;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.UserService;
import io.zaplink.auth.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the UserService interface.
 * Provides user management operations including creation, updates, and validation.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @Service @Transactional @RequiredArgsConstructor
public class UserServiceImpl
    implements
    UserService
{
    private final UserRepository  userRepository;
    private final RoleRepository  roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHelper      userUtility;
    /**
     * Finds a user by their email address.
     * 
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Override @Transactional(readOnly = true)
    public java.util.Optional<User> findByEmail( String email )
    {
        log.debug( LogConstants.LOG_SEARCHING_USER_BY_EMAIL, email );
        return userRepository.findByEmail( email );
    }

    /**
     * Finds a user by their username.
     * 
     * @param username The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Override @Transactional(readOnly = true)
    public java.util.Optional<User> findByUsername( String username )
    {
        log.debug( LogConstants.LOG_SEARCHING_USER_BY_USERNAME, username );
        return userRepository.findByUsername( username );
    }

    /**
     * Creates a new user from registration request.
     * Sets up user details, encodes password, assigns default USER role, and generates verification token.
     * 
     * @param request The user registration request containing user details
     * @return The created User entity
     * @throws UserAlreadyExistsException if user creation fails due to constraints
     */
    @Override
    public User createUser( UserRegistrationRequest request )
    {
        log.info( LogConstants.LOG_CREATING_NEW_USER_ACCOUNT, request.getEmail() );
        // Create user entity using modern record-like pattern
        User user = User.builder().username( request.getUsername() ).email( request.getEmail() )
                .password( passwordEncoder.encode( request.getPassword() ) ).firstName( request.getFirstName() )
                .lastName( request.getLastName() ).phoneNumber( request.getPhoneNumber() ).active( true )
                .verified( false ).verificationToken( Utility.grnerateVerificationCode().toString() )
                .createdAt( Instant.now() ).build();
        // Assign default USER role using Set.of() for immutable sets
        log.debug( LogConstants.LOG_ASSIGNING_DEFAULT_USER_ROLE );
        Role userRole = roleRepository.findByName( "USER" ).orElseGet( () -> {
            log.info( LogConstants.LOG_DEFAULT_USER_ROLE_NOT_FOUND );
            Role role = Role.builder().name( "USER" ).description( "Standard user role" ).build();
            return roleRepository.save( role );
        } );
        user.setRoles( Set.of( userRole ) );
        // Save user
        User savedUser = userRepository.save( user );
        log.info( LogConstants.LOG_USER_CREATED_SUCCESSFULLY, savedUser.getId(), savedUser.getEmail(),
                  savedUser.getUsername() );
        return savedUser;
    }

    /**
     * Saves a user entity to the database.
     * 
     * @param user The user entity to save
     * @return The saved user entity
     */
    @Override
    public User saveUser( User user )
    {
        log.debug( LogConstants.LOG_SAVING_USER_ENTITY, user.getId() );
        return userRepository.save( user );
    }

    /**
     * Checks if a user exists with the given email address.
     * 
     * @param email The email address to check
     * @return true if user exists, false otherwise
     */
    @Override @Transactional(readOnly = true)
    public boolean existsByEmail( String email )
    {
        log.debug( LogConstants.LOG_CHECKING_EMAIL_EXISTS, email );
        return userRepository.existsByEmail( email );
    }

    /**
     * Checks if a user exists with the given username.
     * 
     * @param username The username to check
     * @return true if user exists, false otherwise
     */
    @Override @Transactional(readOnly = true)
    public boolean existsByUsername( String username )
    {
        log.debug( LogConstants.LOG_CHECKING_USERNAME_EXISTS, username );
        return userRepository.existsByUsername( username );
    }

    /**
     * Updates user information.
     * Only updates modifiable fields (firstName, lastName, phoneNumber).
     * 
     * @param userId The ID of the user to update
     * @param updatedUser The user entity with updated information
     * @throws RuntimeException if user is not found
     */
    @Override
    public void updateUser( Long userId, User updatedUser )
    {
        log.info( LogConstants.LOG_UPDATING_USER_INFORMATION, userId );
        // Find existing user using utility
        User existingUser = userUtility.findUserByIdOrThrow( userId, ApiConstants.OPERATION_UPDATE );
        // Update modifiable fields using utility
        userUtility.updateUser( existingUser, user -> {
            user.setFirstName( updatedUser.getFirstName() );
            user.setLastName( updatedUser.getLastName() );
            user.setPhoneNumber( updatedUser.getPhoneNumber() );
        } );
        log.info( LogConstants.LOG_USER_UPDATED_SUCCESSFULLY, userId, existingUser.getEmail() );
    }

    /**
     * Deactivates a user account.
     * User will not be able to log in but their data is preserved.
     * 
     * @param userId The ID of the user to deactivate
     * @throws RuntimeException if user is not found
     */
    @Override
    public void deactivateUser( Long userId )
    {
        log.info( LogConstants.LOG_DEACTIVATING_USER_ACCOUNT, userId );
        User user = userUtility.findUserByIdOrThrow( userId, ApiConstants.OPERATION_DEACTIVATION );
        // Update user using utility
        userUtility.updateUser( user, u -> u.setActive( false ) );
        log.info( LogConstants.LOG_USER_DEACTIVATED_SUCCESSFULLY, userId, user.getEmail() );
    }

    /**
     * Activates a user account.
     * Re-enables login capability for a previously deactivated user.
     * 
     * @param userId The ID of the user to activate
     * @throws RuntimeException if user is not found
     */
    @Override
    public void activateUser( Long userId )
    {
        log.info( LogConstants.LOG_ACTIVATING_USER_ACCOUNT, userId );
        User user = userUtility.findUserByIdOrThrow( userId, ApiConstants.OPERATION_ACTIVATION );
        // Update user using utility
        userUtility.updateUser( user, u -> u.setActive( true ) );
        log.info( LogConstants.LOG_USER_ACTIVATED_SUCCESSFULLY, userId, user.getEmail() );
    }
}
