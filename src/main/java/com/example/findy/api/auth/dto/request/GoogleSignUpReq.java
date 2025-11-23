package com.example.findy.api.auth.dto.request;

import com.example.findy.entity.user.entity.LoginType;

public record GoogleSignUpReq(
        String name,
        String picture,
        String email,
        LoginType type,
        String refreshToken,
        String accessToken
) {
    public static GoogleSignUpReq of(String name, String file, String email, LoginType type, String refreshToken, String accessToken) {
        return new GoogleSignUpReq(name, file, email, type, refreshToken, accessToken);
    }
}
