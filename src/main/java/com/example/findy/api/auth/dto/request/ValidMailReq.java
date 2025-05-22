package com.example.findy.api.auth.dto.request;

import com.example.findy._core.constraint.email.ValidEmail;

public record ValidMailReq(
        @ValidEmail
        String email
) {
}
