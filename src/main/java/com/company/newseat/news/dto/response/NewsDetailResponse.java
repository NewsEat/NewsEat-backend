package com.company.newseat.news.dto.response;

import com.company.newseat.category.domain.Category;
import com.company.newseat.global.util.DateUtil;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;

import java.util.Optional;

public record NewsDetailResponse(
        Long newsId,
        String title,
        String content,
        String imgUrl,
        String publisher,
        String publishedAt,
        String category,
        String sentiment,
        boolean isBookmarked,
        Long bookmarkId

) {
    public static NewsDetailResponse from(News news, Long bookmarkId) {
        String formattedPublishedAt = DateUtil.formatDate(news.getPublished_at());
        boolean isBookmarked = bookmarkId != null;

        String sentiment = Optional.ofNullable(news.getSentiment())
                .map(Sentiment::getDescription)
                .orElse("감정 정보 없음");

        String category = Optional.ofNullable(news.getCategory())
                .map(Category::getName)
                .orElse("카테고리 없음");

        return new NewsDetailResponse(
                news.getNewsId(),
                news.getTitle(),
                news.getContent(),
                news.getImgUrl(),
                news.getPublisher(),
                formattedPublishedAt,
                sentiment,
                category,
                isBookmarked,
                bookmarkId
        );
    }
}
