package io.zaplink.auth.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.exception.UserNotFoundException;
import io.zaplink.auth.common.util.StringUtil;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class CustomUser
    implements
    UserDetailsService
{
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername( String email )
    {
        User user = userRepository.findByEmail( email ).orElseThrow( () -> new UserNotFoundException( StringUtil
                .appendValue( "User not found for email: ", email ) ) );
        if ( !user.isActive() )
            throw new UserNotFoundException( StringUtil
                    .appendValue( ApiConstants.MESSAGE_USER_ACCOUNT_DEACTIVATED_EMAIL, user ) );
        return org.springframework.security.core.userdetails.User.builder().username( user.getEmail() )
                .password( user.getPassword() ).build();
    }
}
