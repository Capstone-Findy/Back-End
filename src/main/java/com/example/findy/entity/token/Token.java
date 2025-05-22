package com.example.findy.entity.token;

import java.time.Instant;

import com.example.findy.entity.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean rememberMe;

    @Transient
    private int accessExpiration;

    @Transient
    private int refreshExpiration;

    private Token(
            User user,
            String accessToken,
            String refreshToken,
            Instant createdAt,
            boolean rememberMe,
            int accessExpiration,
            int refreshExpiration
    ) {
        this.userId = user.getId();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.rememberMe = rememberMe;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public static Token of(
            User user,
            String accessToken,
            String refreshToken,
            Instant createdAt,
            boolean rememberMe,
            int accessExpiration,
            int refreshExpiration
    ) {
        return new Token(
                user,
                accessToken,
                refreshToken,
                createdAt,
                rememberMe,
                accessExpiration,
                refreshExpiration
        );
    }
}
