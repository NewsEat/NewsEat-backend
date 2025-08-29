package com.company.newseat.news.dto.response;

import com.company.newseat.news.domain.News;

public record CategoryNewsResponse(
        Long newsId,
        String title,
        String imgUrl,
        String publisher,
        String publishedAt
) {
    public static CategoryNewsResponse of(News news) {
        return new CategoryNewsResponse(
                news.getNewsId(),
                news.getTitle(),
                news.getImgUrl(),
                news.getPublisher(),
                news.getPublished_at()
        );
    }
}
