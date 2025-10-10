package com.example.findy.entity.token;

import com.example.findy.api.auth.dto.request.KakaoSignUpReq;
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


    private GoogleToken(
            User user,
            String accessToken,
            String refreshToken
    ) {
        this.userId = user.getId();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static GoogleToken of(
            User user,
            KakaoSignUpReq req
    ) {
        return new GoogleToken(
                user,
                req.accessToken(),
                req.refreshToken()
        );
    }

    public static GoogleToken of(
            User user,
            String accessToken,
            String refreshToken
    ){
        return new GoogleToken(user,
                accessToken,
                refreshToken);
    }
}
