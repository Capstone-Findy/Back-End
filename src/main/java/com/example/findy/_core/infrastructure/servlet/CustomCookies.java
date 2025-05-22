package com.example.findy._core.infrastructure.servlet;

import com.findy.processor.RestDocs;
import lombok.Getter;

@Getter
@RestDocs("쿠키 정보")
public enum CustomCookies {
    @RestDocs("Access Token")
    ACCESS_TOKEN("OID_AUT"),

    @RestDocs("Cloud Front 인증 정보 - 정책")
    CLOUD_FRONT_POLICY("CloudFront-Policy"),
    @RestDocs("Cloud Front 인증 정보 -  서명")
    CLOUD_FRONT_SIGNATURE("CloudFront-Signature"),
    @RestDocs("Cloud Front 인증 정보 -  키쌍")
    CLOUD_FRONT_KEY_PAIR_ID("CloudFront-Key-Pair-Id");

    private final String name;

    CustomCookies(String name) {
        this.name = name;
    }

}

