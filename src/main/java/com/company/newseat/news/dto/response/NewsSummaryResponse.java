package com.company.newseat.news.dto.response;

import org.springframework.web.util.HtmlUtils;

public record NewsSummaryResponse (
        String title,
        String sentiment,
        String summaryResult
) {
    public static NewsSummaryResponse of (String title, String sentiment, String summaryResult) {
        return new NewsSummaryResponse(
                HtmlUtils.htmlUnescape(title),
                sentiment,
                summaryResult
        );
    }
}
