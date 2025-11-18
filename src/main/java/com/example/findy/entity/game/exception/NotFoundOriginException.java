package com.example.findy.entity.game.exception;


import com.example.findy._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundOriginException extends ApiException {
    private static final String message = "게임을 찾을 수 없습니다";

    public NotFoundOriginException() {
        super(HttpStatus.NOT_FOUND, message);
    }
}
