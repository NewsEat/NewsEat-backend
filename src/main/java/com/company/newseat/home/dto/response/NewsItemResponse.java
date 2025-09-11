package com.company.newseat.home.dto.response;

public record NewsItemResponse (
        Long newsId,
        String imgUrl,
        String title
) {
}
