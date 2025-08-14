package com.company.newseat.news.dto.response;

import com.company.newseat.news.domain.News;

public record SearchNewsResponse(
        Long newsId,
        String imgUrl,
        String publisher,
        String title,
        String content
) {
    public static SearchNewsResponse of(News news) {
        return new SearchNewsResponse(
                news.getNewsId(),
                news.getImgUrl(),
                news.getPublisher(),
                news.getTitle(),
                news.getContent()
        );
    }
}
