package com.example.findy._core.infrastructure.servlet;

import lombok.Getter;

@Getter
public enum CustomCookies {
    ACCESS_TOKEN("OID_AUT"),

    CLOUD_FRONT_POLICY("CloudFront-Policy"),
    CLOUD_FRONT_SIGNATURE("CloudFront-Signature"),
    CLOUD_FRONT_KEY_PAIR_ID("CloudFront-Key-Pair-Id");

    private final String name;

    CustomCookies(String name) {
        this.name = name;
    }

}

