package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ApiException {
    private static final String message = "만료된 토큰입니다.";

    public ExpiredTokenException() {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
