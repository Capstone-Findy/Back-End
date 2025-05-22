package com.example.findy.entity.user.exception;

import com.example.findy._core.exception.DuplicateException;

public class DuplicateEmailException extends DuplicateException {
    private static final String NAME = "이메일";

    public DuplicateEmailException() {
        super(NAME);
    }

    public DuplicateEmailException(String value) {
        super(NAME, value);
    }
}
