package com.company.newseat.news.dto.response;

public record NewsSummaryResponse (
        String title,
        String sentiment,
        String summaryResult
) {
    public static NewsSummaryResponse of (String title, String sentiment, String summaryResult) {
        return new NewsSummaryResponse(title, sentiment, summaryResult);
    }
}
