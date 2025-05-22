package com.example.findy.api.auth.dto.request;

import com.example.findy._core.constraint.email.ValidEmail;
import com.example.findy._core.constraint.password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpReq(
        @NotBlank @Size(max = 20)
        String name,
        @NotBlank
        String file,
        @ValidEmail
        String email,
        @ValidPassword
        String password
) {
}
