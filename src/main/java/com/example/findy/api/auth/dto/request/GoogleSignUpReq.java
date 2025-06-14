package com.example.findy.api.auth.dto.request;

public record GoogleSignUpReq(
        String name,
        String picture,
        String email,
        String refreshToken,
        String accessToken
) {
    public static GoogleSignUpReq of(
            String name, String picture, String email,
            String refreshToken, String accessToken
    ) {
        return new GoogleSignUpReq(name, picture, email, refreshToken, accessToken);
    }
}
