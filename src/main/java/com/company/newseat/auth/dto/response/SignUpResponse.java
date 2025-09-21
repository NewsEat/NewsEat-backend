package com.company.newseat.auth.dto.response;

import com.company.newseat.user.domain.User;
import lombok.Builder;

@Builder
public record SignUpResponse(
        Long userId,
        String nickname
) {
    public static SignUpResponse of(User user) {
        return SignUpResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }
}