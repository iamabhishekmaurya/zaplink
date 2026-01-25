package io.zaplink.auth.common.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import io.zaplink.auth.common.constants.SecurityConstants;
import io.zaplink.auth.common.util.StringUtil;
import io.zaplink.auth.entity.User;
import io.zaplink.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service @RequiredArgsConstructor
public class CustomUserDetailsService
    implements
    UserDetailsService
{
    private final UserRepository userRepository;
    @Override @Cacheable(value = "auth:user_details", key = "#root.args[0]")
    public UserDetails loadUserByUsername( String email )
        throws UsernameNotFoundException
    {
        log.info( LogConstants.LOG_LOADING_USER_BY_EMAIL, email );
        User user = userRepository.findByEmail( email ).orElseThrow( () -> new UsernameNotFoundException( StringUtil
                .appendValue( ApiConstants.MESSAGE_USER_NOT_FOUND_WITH_EMAIL, email ) ) );
        if ( !user.isActive() )
        {
            throw new UsernameNotFoundException( StringUtil
                    .appendValue( ApiConstants.MESSAGE_USER_ACCOUNT_DEACTIVATED_EMAIL, email ) );
        }
        return new CustomUserDetails( user.getId(),
                                      user.getEmail(),
                                      user.getPassword(),
                                      user.isActive(),
                                      true, // accountNonExpired
                                      true, // credentialsNonExpired
                                      true, // accountNonLocked
                                      user.getRoles().stream().map( role -> role.getName()
                                              .startsWith( SecurityConstants.ROLE_PREFIX ) ? role.getName()
                                                                                           : SecurityConstants.ROLE_PREFIX
                                                                                                   + role.getName() )
                                              .map( SimpleGrantedAuthority::new ).toList() );
    }
}
