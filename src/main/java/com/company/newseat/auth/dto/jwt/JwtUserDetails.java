package com.company.newseat.auth.dto.jwt;

import com.company.newseat.user.domain.User;
import com.company.newseat.user.domain.type.Role;
import io.jsonwebtoken.Claims;
import lombok.Builder;

@Builder
public record JwtUserDetails(
        Long userId,
        Role role
) {
    // JWT에서 파싱한 Claims 객체 -> JwtUserDetails 로 변환
    public static JwtUserDetails fromClaims(Claims claims) {
        return JwtUserDetails.builder()
                .userId(Long.valueOf(claims.getSubject()))
                .role(Role.valueOf(claims.get("role").toString()))
                .build();
    }

    // User -> JwtUserDetails 로 변환
    public static JwtUserDetails fromUser(User user) {
        return JwtUserDetails.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .build();
    }
}
