package com.company.newseat.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest (
        @NotNull(message = "userId 값이 없습니다.")
        Long userId,

        @NotBlank(message = "비밀번호는 필수 입력값입니다")
        @Size(min = 8, max = 16, message = "비밀번호를 8~16자 사이로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+{}|:<>?~,-]{8,16}$",
                message = "비밀번호는 영문과 숫자를 반드시 포함해 8~16자로 입력해주세요.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다")
        @Size(min = 8, max = 16, message = "비밀번호를 8~16자 사이로 입력해주세요.")
        String confirmPassword
) {
}
