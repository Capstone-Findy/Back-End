package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedUserException extends ApiException {
    private static final String message = "인증이 되지 않은 사용자 입니다.";

    public UnauthorizedUserException() {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
