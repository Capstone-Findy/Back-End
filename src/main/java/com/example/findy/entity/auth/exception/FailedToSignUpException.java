package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.BadRequestException;

public class FailedToSignUpException extends BadRequestException {
    private static final String message = "회원가입에 실패하였습니다.";

    public FailedToSignUpException() {
        super(message);
    }

    public FailedToSignUpException(String message) {
        super(message);
    }
}
