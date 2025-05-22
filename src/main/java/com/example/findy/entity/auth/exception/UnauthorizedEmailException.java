package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedEmailException extends ApiException {
    private static final String message = "인증이 되지 않은 이메일 입니다.";

    public UnauthorizedEmailException() {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
