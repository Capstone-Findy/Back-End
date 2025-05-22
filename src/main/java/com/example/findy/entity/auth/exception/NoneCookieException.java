package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NoneCookieException extends ApiException {
    private static final String message = "쿠키가 존재하지 않습니다.";

    public NoneCookieException() {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
