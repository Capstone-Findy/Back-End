package com.example.findy.entity.user.entity;

import com.findy.processor.RestDocs;

@RestDocs("로그인 타입")
public enum LoginType {
    @RestDocs("구글")
    GOOGLE,
    @RestDocs("카카오")
    KAKAO
}
