package io.zaplink.auth;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.zaplink.auth.common.util.JwtUtility;
import io.zaplink.auth.common.util.TokenUtility;

/**
 * Test configuration class for unit tests.
 * Provides mock beans and test-specific configurations.
 */
@TestConfiguration
public class TestConfig
{
    @Bean @Primary
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean @Primary
    public JwtUtility jwtUtility()
    {
        return new JwtUtility();
    }

    @Bean @Primary
    public TokenUtility tokenUtility()
    {
        return new TokenUtility();
    }
}
