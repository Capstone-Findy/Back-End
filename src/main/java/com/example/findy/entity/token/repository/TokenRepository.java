package com.example.findy.entity.token.repository;

import com.example.findy.entity.auth.exception.NotFoundTokenException;
import com.example.findy.entity.token.Token;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);

    void deleteByUserId(long userId);

    boolean existsByUserId(long userId);

    void deleteByAccessToken(String accessToken);

    default @NonNull Token getByAccessTokenAndRefreshToken(@NonNull String accessToken, @NonNull String refreshToken) {
        return findByAccessTokenAndRefreshToken(accessToken, refreshToken).orElseThrow(NotFoundTokenException::new);
    }

    Optional<Token> findByRefreshToken(String refreshToken);

    default Token getByRefreshToken(String refreshToken) {
        return findByRefreshToken(refreshToken).orElseThrow(NotFoundTokenException::new);
    }

    default Token getByUserId(long userId) {
        return findById(userId).orElseThrow(NotFoundTokenException::new);
    }
}
