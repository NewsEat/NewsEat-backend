package com.company.newseat.auth.dto.response;

import com.company.newseat.user.domain.User;
import lombok.Builder;

@Builder
public record SignUpResponse(
        Long userId
) {
    public static SignUpResponse of(User user) {
        return SignUpResponse.builder()
                .userId(user.getUserId())
                .build();
    }
}