package com.example.findy._core.client.kakao.dto.response;

public record KakaoTokenRes(
        String access_token,
        String refresh_token
) {
}
