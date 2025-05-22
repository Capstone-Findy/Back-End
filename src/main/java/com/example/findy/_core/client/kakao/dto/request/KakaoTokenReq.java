package com.example.findy._core.client.kakao.dto.request;

public record KakaoTokenReq(
        String grant_type,
        String client_id,
        String redirect_uri,
        String code,
        String client_secret
) {
    public static KakaoTokenReq of(String grant_type, String client_id, String redirect_uri, String code, String client_secret) {
        return new KakaoTokenReq(grant_type, client_id, redirect_uri, code, client_secret);
    }
}
