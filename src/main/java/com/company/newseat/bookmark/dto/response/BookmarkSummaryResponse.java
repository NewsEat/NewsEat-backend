package com.company.newseat.bookmark.dto.response;

import org.springframework.web.util.HtmlUtils;

public record BookmarkSummaryResponse(
        String title,
        String sentiment,
        String summaryResult
) {
    public static BookmarkSummaryResponse of (String title, String sentiment, String summaryResult) {
        return new BookmarkSummaryResponse(
                HtmlUtils.htmlUnescape(title),
                sentiment,
                summaryResult
        );
    }
}