package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FailedToSignInException extends ApiException {
    private static final String message = "로그인에 실패하였습니다.";

    public FailedToSignInException() {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
