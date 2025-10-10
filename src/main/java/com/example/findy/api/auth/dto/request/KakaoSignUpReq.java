package com.example.findy.api.auth.dto.request;

import com.example.findy.entity.user.entity.LoginType;

public record KakaoSignUpReq(
        String name,
        String file,
        String email,
        LoginType type,
        String refreshToken,
        String accessToken
) {
    public static KakaoSignUpReq of(String name, String file, String email, LoginType type, String refreshToken, String accessToken) {
        return new KakaoSignUpReq(name, file, email, type, refreshToken, accessToken);
    }
}
