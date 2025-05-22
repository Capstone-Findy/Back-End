package com.example.findy._core.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring")
public record SpringProperties(
        MailProperties mail
) {
    public record MailProperties(
            String host,
            int port,
            String username,
            String password,
            int expiration
    ){

    }
}
