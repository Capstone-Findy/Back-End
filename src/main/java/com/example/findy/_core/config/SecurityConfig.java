package com.example.findy._core.config;

import java.util.List;

import com.example.findy._core.environment.SecurityProperties;
import com.example.findy._core.infrastructure.filter.HttpServletWrapperFilter;
import com.example.findy._core.infrastructure.filter.JwtAuthenticationFilter;
import com.example.findy._core.infrastructure.handler.JwtAccessDeniedHandler;
import com.example.findy._core.infrastructure.handler.JwtAuthenticationEntryPoint;
import com.example.findy.api.auth.service.JwtProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {
    public static final String[] EXCLUDE_PATH = {
            "/test/**",
            "/auth/sign-in/**",
            "/auth/sign-up/**",
            "/auth/find/**",
            "/auth/password",
            "/auth/refresh",
            "/auth/valid/**",
            "/auth/kakao/sign-up",
            "/api",
            "/error/**",
            "/courses/**",
            "/docs/**",
            "/favicon.ico",
            "/static/**"
    };
    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityProperties.CorsProperties corsProperties;
    private final SecurityProperties.CookieProperties cookieProperties;

    public SecurityConfig(
            JwtProvider jwtProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            SecurityProperties securityProperties
    ) {
        this.jwtProvider = jwtProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.corsProperties = securityProperties.cors();
        this.cookieProperties = securityProperties.cookie();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'"))
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .requestMatchers("/auth/**")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), AuthorizationFilter.class)
                .addFilterBefore(new HttpServletWrapperFilter(cookieProperties), ExceptionTranslationFilter.class)
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        final String[] allowedMethod = {"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(corsProperties.origins()));
        corsConfiguration.setAllowedMethods(List.of(allowedMethod));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
