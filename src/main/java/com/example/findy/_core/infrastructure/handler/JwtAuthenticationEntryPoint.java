package com.example.findy._core.infrastructure.handler;

import java.io.IOException;

import com.example.findy._core.environment.SecurityProperties;
import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityProperties securityProperties;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        WebClientResponse clientResponse = new WebClientResponse(response, securityProperties.cookie());
        clientResponse.setException(HttpStatus.UNAUTHORIZED, authException);
    }
}
