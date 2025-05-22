package com.example.findy._core.infrastructure.servlet;

import java.io.IOException;

import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.environment.SecurityProperties;
import com.example.findy._core.exception.ApiException;
import com.example.findy.entity.token.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebClientResponse extends HttpServletResponseWrapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final        boolean      secure;

    public WebClientResponse(HttpServletResponse response, SecurityProperties.CookieProperties cookieProperties) {
        super(response);
        this.secure = cookieProperties.secure();
    }

    public void removeAllCustomCookies() {
        for (CustomCookies cookie : CustomCookies.values()) {
            removeCookie(cookie);
        }
    }

    public void addTokenCookies(WebClientResponse res, Token token){
        Cookie cookie = accessToken(token);
        res.addCookie(cookie);
    }

    private Cookie accessToken(Token token) {
        Cookie cookie = new Cookie(CustomCookies.ACCESS_TOKEN.getName(), token.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(token.getAccessExpiration());
        return cookie;
    }

    private void removeCookie(CustomCookies customCookie) {
        Cookie cookie = new Cookie(customCookie.getName(), null);
        cookie.setMaxAge(0);
        super.addCookie(cookie);
    }

    public void setException(ApiException e) throws IOException {
        this.setException(ApiResponse.failedOf(e));
    }

    public void setException(Exception e) throws IOException {
        this.setException(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public void setException(HttpStatus status, Exception e) throws IOException {
        this.setException(ApiResponse.failedOf(status, e.getMessage()));
    }

    private void setException(ResponseEntity<?> response) throws IOException {
        super.setStatus(response.getStatusCode().value());
        super.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        super.setCharacterEncoding("UTF-8");
        super.getWriter().write(objectMapper.writeValueAsString(response.getBody()));
    }

}
