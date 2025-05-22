package com.example.findy._core.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        JwtProperties jwt,
        CorsProperties cors,
        EncryptionProperties encryption,
        CookieProperties cookie
) {
    public record JwtProperties(
            AccessProperties access,
            RefreshProperties refresh
    ) {
        public record AccessProperties(
                String secret,
                int expiration
        ) {}

        public record RefreshProperties(
                String secret,
                int expirationDefault,
                int expirationRememberMe
        ) {}
    }

    public record CorsProperties(
            String[] origins
    ) {
    }

    public record EncryptionProperties(
            String secretKey
    ) {}

    public record CookieProperties(
            boolean secure
    ) {}
}
