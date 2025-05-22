package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundTokenException extends ApiException {
    private static final String message = "토큰을 찾을 수 없습니다.";

    public NotFoundTokenException() {
        super(HttpStatus.NOT_FOUND, message);
    }
}
