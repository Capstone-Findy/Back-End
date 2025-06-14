package com.example.findy.entity.token;

import com.example.findy.api.auth.dto.request.GoogleSignUpReq;
import com.example.findy.entity.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleToken {

    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public static GoogleToken of(User user, GoogleSignUpReq req) {
        return new GoogleToken(user.getId(), req.accessToken(), req.refreshToken());
    }

    public static GoogleToken of(User user, String accessToken, String refreshToken) {
        return new GoogleToken(user.getId(), accessToken, refreshToken);
    }

    private GoogleToken(long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
