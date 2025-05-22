package com.example.findy._core.exception;

import org.springframework.http.HttpStatus;

public class CannotDeleteInUsedException extends ApiException {

    public CannotDeleteInUsedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}