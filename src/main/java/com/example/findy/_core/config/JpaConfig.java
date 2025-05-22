package com.example.findy._core.config;

import com.example.findy.api.auth.dto.JwtAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAware<>() {
            @Override
            public @NonNull Optional<Long> getCurrentAuditor() {
                return JwtAuthentication.findUserId();
            }
        };
    }

}
