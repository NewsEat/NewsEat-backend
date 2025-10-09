package com.company.newseat.news.dto.response;

public record SentimentAnalysisResponse(
        Long newsId,
        String sentiment
) {
}
