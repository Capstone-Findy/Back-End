package com.example.findy.api.auth.dto.request;

import com.example.findy._core.constraint.email.ValidEmail;
import com.example.findy._core.constraint.password.ValidPassword;
import jakarta.validation.constraints.NotNull;

public record SignInReq(
        @ValidEmail
        String email,
        @ValidPassword
        String password,
        @NotNull
        boolean rememberMe
) {
}
