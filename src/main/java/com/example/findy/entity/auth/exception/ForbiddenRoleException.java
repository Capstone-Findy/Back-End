package com.example.findy.entity.auth.exception;

import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ForbiddenRoleException extends ApiException {
    private static final String message = "접근 권한이 없습니다.";

    public ForbiddenRoleException() {
        super(HttpStatus.FORBIDDEN, message);
    }
}
