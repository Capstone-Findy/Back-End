package com.example.findy.entity.token.repository;

import com.example.findy.entity.token.GoogleToken;
import com.example.findy.entity.auth.exception.NotFoundTokenException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Long> {
    default GoogleToken getByUserId(long userId) {
        return findByUserId(userId).orElseThrow(NotFoundTokenException::new);
    }

    Optional<GoogleToken> findByUserId(long userId);
}
