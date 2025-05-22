package com.example.findy.api.auth.dto;

import io.jsonwebtoken.Claims;

public class JwtClaims {
    private final Claims claims;

    public JwtClaims(Claims claims) {
        this.claims = claims;
    }

    public long getUserId() {
        return Long.parseLong(claims.getSubject());
    }
}
