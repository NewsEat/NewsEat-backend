package com.company.newseat.auth.dto.request;

import com.company.newseat.email.domain.EmailAuth;
import com.company.newseat.email.domain.type.BooleanType;
import com.company.newseat.email.domain.type.Purpose;
import com.company.newseat.global.domain.type.Status;
import com.company.newseat.global.validation.annotation.ValidPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SendEmailCodeRequest (
        @NotBlank(message = "이메일은 필수 입력값입니다")
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,

        @ValidPurpose
        int purpose
) {
    public static EmailAuth toEmailAuth(SendEmailCodeRequest request) {
        return EmailAuth.builder()
                .email(request.email())
                .purpose(Purpose.of(request.purpose()))
                .isChecked(BooleanType.F)
                .status(Status.ACTIVE)
                .build();
    }
}
