package com.company.newseat.news.dto.response;

import com.company.newseat.global.util.DateUtil;
import com.company.newseat.news.domain.News;
import org.springframework.web.util.HtmlUtils;

public record SearchNewsResponse(
        Long newsId,
        String imgUrl,
        String publisher,
        String title,
        String publishedAt
) {
    public static SearchNewsResponse from (News news) {
        String formattedPublishedAt = DateUtil.formatDate(news.getPublished_at());
        return new SearchNewsResponse(
                news.getNewsId(),
                news.getImgUrl(),
                news.getPublisher(),
                HtmlUtils.htmlUnescape(news.getTitle()),
                formattedPublishedAt
        );
    }
}
