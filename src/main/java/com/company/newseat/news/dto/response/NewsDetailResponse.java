package com.company.newseat.news.dto.response;

import com.company.newseat.global.util.DateUtil;
import com.company.newseat.news.domain.News;

public record NewsDetailResponse(
        Long newsId,
        String title,
        String content,
        String imgUrl,
        String publisher,
        String publishedAt,
        String category,
        String sentiment,
        boolean isBookmarked

) {
    public static NewsDetailResponse of (News news, boolean isBookmarked) {
        String formattedPublishedAt = DateUtil.formatDate(news.getPublished_at());

        return new NewsDetailResponse(
                news.getNewsId(),
                news.getTitle(),
                news.getContent(),
                news.getImgUrl(),
                news.getPublisher(),
                formattedPublishedAt,
                news.getCategory().getName(),
                news.getSentiment().getDescription(),
                isBookmarked
        );
    }
}
