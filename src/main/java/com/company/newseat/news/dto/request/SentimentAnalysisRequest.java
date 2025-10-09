package com.company.newseat.news.dto.request;

public record SentimentAnalysisRequest(
        Long news_id,
        String title,
        String content
) {
}
