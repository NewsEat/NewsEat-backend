package com.company.newseat.auth.dto.jwt;

public record Tokens(
        String accessToken,
        String refreshToken
) {
}
