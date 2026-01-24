package io.zaplink.auth.common.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CustomUserDetails
    extends
    User
{
    private final Long id;
    @JsonCreator
    public CustomUserDetails( @JsonProperty("id") Long id,
                              @JsonProperty("username") String username,
                              @JsonProperty("password") String password,
                              @JsonProperty("enabled") boolean enabled,
                              @JsonProperty("accountNonExpired") boolean accountNonExpired,
                              @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
                              @JsonProperty("accountNonLocked") boolean accountNonLocked,
                              @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities )
    {
        super( username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities );
        this.id = id;
    }
}
