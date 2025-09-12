package com.company.newseat.news.dto.response;

public record NewsItemResponse (
        Long newsId,
        String imgUrl,
        String title
) {
}
