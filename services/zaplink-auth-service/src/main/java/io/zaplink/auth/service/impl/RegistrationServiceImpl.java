package io.zaplink.auth.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.exception.UserAlreadyExistsException;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.common.util.StringUtil;
import io.zaplink.auth.dto.request.EmailRequest;
import io.zaplink.auth.dto.request.UserRegistrationRequest;
import io.zaplink.auth.dto.response.UserRegistrationResponse;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import io.zaplink.auth.service.RegistrationService;
import io.zaplink.auth.service.UserService;
import io.zaplink.auth.service.helper.KafkaServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the RegistrationService interface.
 * Handles user registration, email verification, and account management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j @Service @Transactional @RequiredArgsConstructor
public class RegistrationServiceImpl
    implements
    RegistrationService
{
    private final UserService        userService;
    private final UserRepository     userRepository;
    private final KafkaServiceHelper kafkaServiceHelper;
    /**
     * Registers a new user in the system.
     * Performs validation for existing users and creates user with verification token.
     * 
     * @param request The user registration request containing user details
     * @return UserRegistrationResponse with created user information
     * @throws UserAlreadyExistsException if user with email or username already exists
     */
    @Override
    public UserRegistrationResponse registerUser( UserRegistrationRequest request )
    {
        log.info( LogConstants.LOG_STARTING_USER_REGISTRATION, request.getEmail() );
        // Validate email uniqueness
        log.debug( LogConstants.LOG_CHECKING_EMAIL_EXISTS, request.getEmail() );
        if ( userService.existsByEmail( request.getEmail() ) )
        {
            log.warn( LogConstants.LOG_REGISTRATION_ATTEMPT_EXISTING_EMAIL, request.getEmail() );
            throw new UserAlreadyExistsException( StringUtil.concat( ApiConstants.MESSAGE_USER_EMAIL_ALREADY_EXISTS,
                                                                     request.getEmail(),
                                                                     ApiConstants.MESSAGE_ALREADY_EXISTS_SUFFIX ) );
        }
        // Validate username uniqueness
        log.debug( LogConstants.LOG_CHECKING_USERNAME_EXISTS, request.getUsername() );
        if ( userService.existsByUsername( request.getUsername() ) )
        {
            log.warn( LogConstants.LOG_REGISTRATION_ATTEMPT_EXISTING_USERNAME, request.getUsername() );
            throw new UserAlreadyExistsException( StringUtil.concat( ApiConstants.MESSAGE_USER_USERNAME_ALREADY_EXISTS,
                                                                     request.getUsername(),
                                                                     ApiConstants.MESSAGE_ALREADY_EXISTS_SUFFIX ) );
        }
        // Create new user
        log.debug( LogConstants.LOG_CREATING_NEW_USER_ACCOUNT );
        User user = userService.createUser( request );
        log.info( LogConstants.LOG_USER_REGISTERED_SUCCESSFULLY, user.getId(), user.getEmail(), user.getUsername() );
        EmailRequest emailRequest = EmailRequest.builder().to( user.getEmail() ).subject( "Zaplink Verification Email" )
                .body( user.getVerificationToken() ).build();
        kafkaServiceHelper.sendMessage( emailRequest );
        log.debug( LogConstants.LOG_VERIFICATION_EMAIL_SHOULD_BE_SENT, user.getEmail() );
        // Build response using modern SuperBuilder pattern
        UserRegistrationResponse response = UserRegistrationResponse.builder().success( true )
                .message( ApiConstants.MESSAGE_USER_REGISTERED_SUCCESSFULLY ).userId( user.getId() )
                .username( user.getUsername() ).email( user.getEmail() ).firstName( user.getFirstName() )
                .lastName( user.getLastName() ).verified( user.isVerified() ).createdAt( user.getCreatedAt() ).build();
        log.debug( LogConstants.LOG_USER_REGISTRATION_RESPONSE_CREATED, user.getId() );
        return response;
    }

    /**
     * Verifies a user's email address using a verification token.
     * Marks the user as verified and removes the verification token.
     * 
     * @param token The email verification token
     * @throws UserNotFoundException if token is invalid or not found
     */
    @Override
    public void verifyEmail( String token )
    {
        log.info( LogConstants.LOG_PROCESSING_EMAIL_VERIFICATION,
                  token.substring( 0, Math.min( 10, token.length() ) ) + ApiConstants.MESSAGE_TOKEN_TRUNCATED_SUFFIX );
        // Find user by verification token
        User user = userRepository.findByVerificationToken( token ).orElseThrow( () -> {
            log.warn( LogConstants.LOG_EMAIL_VERIFICATION_INVALID_TOKEN,
                      token.substring( 0, Math.min( 10, token.length() ) )
                              + ApiConstants.MESSAGE_TOKEN_TRUNCATED_SUFFIX );
            return new UserNotFoundException( ApiConstants.MESSAGE_INVALID_VERIFICATION_TOKEN );
        } );
        // Check if already verified
        if ( user.isVerified() )
        {
            log.info( LogConstants.LOG_EMAIL_ALREADY_VERIFIED, user.getEmail(), user.getId() );
            return;
        }
        // Mark user as verified
        user.setVerified( true );
        user.setVerificationToken( null );
        userRepository.save( user );
        log.info( LogConstants.LOG_EMAIL_VERIFIED_SUCCESSFULLY, user.getEmail(), user.getId() );
    }

    /**
     * Resends the verification email to a user.
     * Generates a new verification token if the user exists and is not yet verified.
     * 
     * @param email The email address to send verification to
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public void resendVerificationEmail( String email )
    {
        log.info( LogConstants.LOG_PROCESSING_RESEND_VERIFICATION_EMAIL, email );
        // Find user by email
        User user = userRepository.findByEmail( email ).orElseThrow( () -> {
            log.warn( LogConstants.LOG_RESEND_VERIFICATION_NON_EXISTENT_EMAIL, email );
            return new UserNotFoundException( StringUtil.createMessage( ApiConstants.MESSAGE_USER_NOT_FOUND_WITH_EMAIL,
                                                                        email ) );
        } );
        // Check if already verified
        if ( user.isVerified() )
        {
            log.info( LogConstants.LOG_USER_ALREADY_VERIFIED, email );
            return;
        }
        // Generate new verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken( verificationToken );
        userRepository.save( user );
        log.info( LogConstants.LOG_NEW_VERIFICATION_TOKEN_GENERATED, email, user.getId() );
        // TODO: Send verification email
        log.debug( LogConstants.LOG_VERIFICATION_EMAIL_SHOULD_BE_SENT, email );
    }
}
