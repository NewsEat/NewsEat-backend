package com.company.newseat.user.dto;

import java.util.List;

public record MypageProfileResponse(
        String nickname,
        List<String> categories
) {
}
