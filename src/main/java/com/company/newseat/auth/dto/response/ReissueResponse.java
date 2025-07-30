package com.company.newseat.auth.dto.response;

import com.company.newseat.auth.dto.jwt.Tokens;
import lombok.Builder;

@Builder
public record ReissueResponse(
        String accessToken
) {
    public static ReissueResponse from(Tokens token) {
        return ReissueResponse.builder()
                .accessToken(token.accessToken())
                .build();
    }
}