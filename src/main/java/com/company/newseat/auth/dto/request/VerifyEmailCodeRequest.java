package com.company.newseat.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailCodeRequest(
        @NotNull(message = "emailAuthId 값이 없습니다.")
        Long emailAuthId,

        @NotNull(message = "emailAuthCode 값이 없습니다.")
        @Pattern(regexp = "\\d{6}", message = "인증 코드는 숫자 6자리로만 구성되어야 합니다.")
        String emailAuthCode
)  {
}