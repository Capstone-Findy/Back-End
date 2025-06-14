package com.example.findy._core.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "externals")
public record ExternalsProperties(
        RedisProperties redis,
        KakaoProperties kakao,
        GoogleProperties google
) {
    public record RedisProperties(
            String host,
            int port,
            String password
    ) {
    }

    public record KakaoProperties(
            String type,
            String clientSecret,
            String clientId
    ) {
    }

    public record GoogleProperties(
            String client_id,
            String client_secret,
            String redirect_uri
    ) {}
}