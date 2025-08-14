package com.company.newseat.news.dto.response;

public record NewsSummaryResponse (
        String summaryResult
) {
    public static NewsSummaryResponse of (String summaryResult) {
        return new NewsSummaryResponse(summaryResult);
    }
}
