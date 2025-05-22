package com.example.findy._core.infrastructure.filter;

import java.io.IOException;

import com.example.findy._core.environment.SecurityProperties;
import com.example.findy._core.infrastructure.servlet.WebClientRequest;
import com.example.findy._core.infrastructure.servlet.WebClientResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

public class HttpServletWrapperFilter extends OncePerRequestFilter {

    private final SecurityProperties.CookieProperties cookieProperties;

    public HttpServletWrapperFilter(SecurityProperties.CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        filterChain.doFilter(new WebClientRequest(request), new WebClientResponse(response, cookieProperties));
    }
}
