package io.zaplink.auth.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration @EnableJpaAuditing @EnableJpaRepositories(basePackages = "io.zaplink.auth.repository")
public class JpaConfig
{
}
