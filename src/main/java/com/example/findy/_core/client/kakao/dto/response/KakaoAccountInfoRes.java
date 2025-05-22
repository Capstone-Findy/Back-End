package com.example.findy._core.client.kakao.dto.response;

public record KakaoAccountInfoRes(
    KakaoAccount kakao_account
) {
    public record KakaoAccount(
            String email,
            Profile profile
    ){

    }

    public record Profile(
            String nickname,
            String profile_image_url
    ){

    }
}
