package com.example.findy._core.exception;

import org.springframework.http.HttpStatus;

public class CannotDeactivateInUseException extends ApiException {

    public CannotDeactivateInUseException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}