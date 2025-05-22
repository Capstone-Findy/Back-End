package com.example.findy._core.infrastructure.handler;

import java.io.IOException;

import com.example.findy._core.environment.SecurityProperties;
import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityProperties securityProperties;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        WebClientResponse clientResponse = new WebClientResponse(response, securityProperties.cookie());
        clientResponse.setException(HttpStatus.FORBIDDEN, accessDeniedException);
    }
}
