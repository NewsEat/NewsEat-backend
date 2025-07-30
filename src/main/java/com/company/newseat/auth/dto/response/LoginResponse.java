package com.company.newseat.auth.dto.response;

import com.company.newseat.auth.dto.jwt.Tokens;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.domain.type.Role;
import lombok.Builder;

@Builder
public record LoginResponse(
        Role role,
        String accessToken
) {
    public static LoginResponse of(Tokens token, User user) {
        return LoginResponse.builder()
                .role(user.getRole())
                .accessToken(token.accessToken())
                .build();
    }
}
