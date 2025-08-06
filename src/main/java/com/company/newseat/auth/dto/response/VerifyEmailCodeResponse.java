package com.company.newseat.auth.dto.response;

import lombok.Builder;

@Builder
public record VerifyEmailCodeResponse(
        boolean isChecked
) {
    public static VerifyEmailCodeResponse of(boolean isChecked) {
        return VerifyEmailCodeResponse.builder()
                .isChecked(isChecked)
                .build();
    }
}
