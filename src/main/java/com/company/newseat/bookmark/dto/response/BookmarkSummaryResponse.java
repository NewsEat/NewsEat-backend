package com.company.newseat.bookmark.dto.response;

public record BookmarkSummaryResponse(
        String title,
        String sentiment,
        String summaryResult
) {
    public static BookmarkSummaryResponse of (String title, String sentiment, String summaryResult) {
        return new BookmarkSummaryResponse(title, sentiment, summaryResult);
    }
}