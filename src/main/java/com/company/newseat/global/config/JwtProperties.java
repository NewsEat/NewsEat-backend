package com.company.newseat.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jwt")
public record JwtProperties(
        String secretKey,
        String issuer,
        Long accessTokenExpiration,
        Long refreshTokenExpiration
) {
}
