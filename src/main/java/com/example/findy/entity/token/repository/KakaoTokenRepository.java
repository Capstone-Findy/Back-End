package com.example.findy.entity.token.repository;

import com.example.findy.entity.auth.exception.NotFoundTokenException;
import com.example.findy.entity.token.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    default KakaoToken getByUserId(long userId) {
        return findByUserId(userId).orElseThrow(NotFoundTokenException::new);
    }

    Optional<KakaoToken> findByUserId(long userId);
}
