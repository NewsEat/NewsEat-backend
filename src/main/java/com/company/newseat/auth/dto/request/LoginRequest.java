package com.company.newseat.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다")
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,

        @Size(min = 8, max = 16, message = "비밀번호를 8자 이상 16자 이하로 입력해주세요")
        @NotBlank(message = "이메일은 필수 입력값입니다")
        String password
) {
}