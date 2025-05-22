package com.example.findy._testutils.fixture;


import com.example.findy._core.environment.SecurityProperties;

public class PropertiesFixture {
    public static SecurityProperties securityProperties() {
        return new SecurityProperties(
                new SecurityProperties.JwtProperties(
                        new SecurityProperties.JwtProperties.AccessProperties("secret", 1),
                        new SecurityProperties.JwtProperties.RefreshProperties("secret", 1, 1)
                ),
                new SecurityProperties.CorsProperties(new String[]{""}),
                new SecurityProperties.EncryptionProperties("H6VNzJK6EY4Kk+uvCxCqAupkcB53uEo1QxQ6ub9eVEs="),
                new SecurityProperties.CookieProperties(true)
        );
    }
}
