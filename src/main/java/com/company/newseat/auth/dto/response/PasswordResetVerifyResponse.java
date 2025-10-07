package com.company.newseat.auth.dto.response;

public record PasswordResetVerifyResponse(
        Long userId
) {
    public static PasswordResetVerifyResponse of(Long userId) {
        return new PasswordResetVerifyResponse(userId);
    }
}
