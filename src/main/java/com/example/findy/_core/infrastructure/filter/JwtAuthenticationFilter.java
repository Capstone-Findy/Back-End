package com.example.findy._core.infrastructure.filter;

import java.io.IOException;
import java.util.Arrays;

import com.example.findy._core.config.SecurityConfig;
import com.example.findy._core.infrastructure.servlet.WebClientRequest;
import com.example.findy.api.auth.dto.JwtAuthentication;
import com.example.findy.api.auth.dto.JwtClaims;
import com.example.findy.api.auth.service.JwtProvider;
import com.example.findy.entity.auth.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher;

    public JwtAuthenticationFilter(
            @Autowired JwtProvider jwtProvider
    ) {
        this.jwtProvider = jwtProvider;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = ((WebClientRequest) request).getAccessToken();

            jwtProvider.checkSignOutToken(token);
            // fixme: jwt 라이브러리 자체 error 메시지를 그대로 출력하게됨. 이 부분 수정해야함.
            JwtClaims claims = jwtProvider.parseToken(token);
            JwtAuthentication jwtAuthentication = JwtAuthentication.of(request, claims);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        } catch (Exception e) {
            throw new JwtAuthenticationException(e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
