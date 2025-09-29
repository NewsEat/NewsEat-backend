package com.company.newseat.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record PasswordResetVerifyRequest(
        @NotNull(message = "emailAuthId 값이 없습니다.")
        Long emailAuthId
) {
}
