package com.company.newseat.auth.dto.request;

import com.company.newseat.global.validation.annotation.ValidCategoryIdRange;
import com.company.newseat.user.domain.User;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.company.newseat.user.domain.type.Provider.SELF;
import static com.company.newseat.user.domain.type.Role.USER;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다")
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다")
        @Size(min = 8, max = 20, message = "비밀번호를 8~20자 사이로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+{}|:<>?~,-]{8,16}$",
                message = "비밀번호는 영문과 숫자를 반드시 포함해 8~16자로 입력해주세요.")
        String password,

        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "닉네임은 한국어/영어/숫자로 최대 10자 입력 가능합니다.")
        String nickname,

        @ValidCategoryIdRange
        @Size(min = 1, max = 3, message = "관심 카테고리는 최소 1개, 최대 3개까지 선택 가능합니다.")
        List<Long> categoryIds

) {
    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .provider(SELF)
                .nickname(nickname)
                .role(USER)
                .isDetox(false)
                .build();
    }
}
