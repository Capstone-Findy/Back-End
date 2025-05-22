package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    private static final String message = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
