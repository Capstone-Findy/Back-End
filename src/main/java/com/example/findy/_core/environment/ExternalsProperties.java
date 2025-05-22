package com.example.findy._core.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "externals")
public record ExternalsProperties(
        RedisProperties redis,
        KakaoProperties kakao
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
}