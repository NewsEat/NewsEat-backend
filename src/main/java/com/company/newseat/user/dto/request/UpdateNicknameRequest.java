package com.company.newseat.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateNicknameRequest(
        @NotBlank(message = "닉네임을 입력해주세요")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$",
                message = "닉네임은 한국어/영어/숫자로 최대 10자 입력 가능합니다.")
        String nickname
) {
}
