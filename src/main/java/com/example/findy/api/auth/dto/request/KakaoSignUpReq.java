package com.example.findy.api.auth.dto.request;

public record KakaoSignUpReq(
        String name,
        String file,
        String email,
        String password,
        String refreshToken,
        String accessToken
) {
    public static KakaoSignUpReq of(String name, String file, String email, String password, String refreshToken, String accessToken) {
        return new KakaoSignUpReq(name, file, email, password, refreshToken, accessToken);
    }
}
