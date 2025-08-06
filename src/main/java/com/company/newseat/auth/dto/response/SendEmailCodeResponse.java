package com.company.newseat.auth.dto.response;

import com.company.newseat.email.domain.EmailAuth;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SendEmailCodeResponse(
        Long emailAuthId,
        LocalDateTime createDate
) {
    public static SendEmailCodeResponse of(EmailAuth emailAuth) {
        return SendEmailCodeResponse.builder()
                .emailAuthId(emailAuth.getEmailAuthId())
                .createDate(emailAuth.getCreatedDate())
                .build();
    }
}
