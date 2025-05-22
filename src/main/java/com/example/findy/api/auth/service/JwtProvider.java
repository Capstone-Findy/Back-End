package com.example.findy.api.auth.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.example.findy._core.client.kakao.KakaoClient;
import com.example.findy._core.client.kakao.dto.response.KakaoTokenRes;
import com.example.findy._core.environment.SecurityProperties;
import com.example.findy.api.auth.dto.JwtClaims;
import com.example.findy.entity.auth.exception.InvalidTokenException;
import com.example.findy.entity.token.KakaoToken;
import com.example.findy.entity.token.Token;
import com.example.findy.entity.token.repository.KakaoTokenRepository;
import com.example.findy.entity.token.repository.TokenRepository;
import com.example.findy.entity.user.entity.User;
import com.example.findy.entity.auth.repository.BlackListRepository;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JwtProvider {
    private final BlackListRepository blackListRepository;
    private final TokenRepository tokenRepository;
    private final KakaoClient kakaoClient;
    private final SecurityProperties.JwtProperties jwtProperties;
    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final KakaoTokenRepository kakaoTokenRepository;

    public JwtProvider(
            BlackListRepository blackListRepository,
            TokenRepository tokenRepository,
            SecurityProperties securityProperties,
            KakaoClient kakaoClient,
            KakaoTokenRepository kakaoTokenRepository
    ) {
        this.blackListRepository = blackListRepository;
        this.tokenRepository = tokenRepository;
        this.jwtProperties = securityProperties.jwt();
        this.kakaoClient = kakaoClient;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.accessSecretKey = new SecretKeySpec(
                jwtProperties.access().secret().getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.refreshSecretKey = new SecretKeySpec(
                jwtProperties.refresh().secret().getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    @Transactional
    public Token issueTokens(User user, boolean rememberMe) {
        Instant now = Instant.now();
        String accessToken = createAccessToken(user, now);
        String refreshToken = createRefreshToken(rememberMe, now);

        if(tokenRepository.existsByUserId(user.getId()))
            tokenRepository.deleteByUserId(user.getId());

        Token token = Token.of(
                user,
                accessToken,
                refreshToken,
                now,
                rememberMe,
                jwtProperties.access().expiration(),
                getRefreshExpiration(rememberMe)
        );

        tokenRepository.save(token);

        return token;
    }

    @Transactional
    public KakaoToken issueKakaoTokens(User user) {
        KakaoToken kakaoToken = kakaoTokenRepository.getByUserId(user.getId());
        KakaoTokenRes res = kakaoClient.refreshToken(kakaoToken.getRefreshToken()).block();
        KakaoToken token = KakaoToken.of(user,
                res.access_token(),
                res.refresh_token() == null ? kakaoToken.getRefreshToken() : res.refresh_token());

        kakaoTokenRepository.delete(kakaoToken);
        kakaoTokenRepository.save(token);

        return token;
    }

    @Transactional(readOnly = true)
    public void checkSignOutToken(String accessToken) {
        blackListRepository.findByAccessToken(accessToken).ifPresent(blackList -> {
            throw new InvalidTokenException();
        });
    }

    public JwtClaims parseToken(
            String accessToken
    ) throws ExpiredJwtException, SecurityException, MalformedJwtException, UnsupportedJwtException, IllegalArgumentException {
        return new JwtClaims(
                Jwts
                        .parser()
                        .verifyWith(accessSecretKey)
                        .build()
                        .parseSignedClaims(accessToken)
                        .getPayload()
        );
    }

    protected String createAccessToken(User user, Instant now) {
        JwtBuilder builder = Jwts.builder()
                .subject(Long.toString(user.getId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.access().expiration())))
                .signWith(accessSecretKey);

        return builder.compact();
    }

    protected String createRefreshToken(boolean rememberMe, Instant now) {
        return Jwts
                .builder()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(getRefreshExpiration(rememberMe))))
                .signWith(refreshSecretKey)
                .compact();
    }

    protected int getRefreshExpiration(boolean rememberMe) {
        return rememberMe ? jwtProperties.refresh().expirationRememberMe() : jwtProperties
                .refresh()
                .expirationDefault();
    }
}
