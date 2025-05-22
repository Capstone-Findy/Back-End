package com.example.findy._core.infrastructure.servlet;

import java.util.Arrays;
import java.util.Optional;

import com.example.findy.entity.auth.exception.NoneCookieException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class WebClientRequest extends HttpServletRequestWrapper {

    public WebClientRequest(HttpServletRequest request) {
        super(request);
    }

    public String getAccessToken() {
        return findAccessToken().orElseThrow(NoneCookieException::new);
    }

    private Optional<String> findAccessToken() {
        Cookie[] cookies = super.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays
                .stream(super.getCookies())
                .filter(cookie -> CustomCookies.ACCESS_TOKEN.getName().equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}