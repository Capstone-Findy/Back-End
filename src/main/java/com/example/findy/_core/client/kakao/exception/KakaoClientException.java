package com.example.findy._core.client.kakao.exception;

import com.example.findy._core.exception.ExternalApiException;

public class KakaoClientException extends ExternalApiException {

    public KakaoClientException(String message) {
        super(message);
    }

}
