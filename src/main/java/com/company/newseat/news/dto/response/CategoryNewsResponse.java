package com.company.newseat.news.dto.response;

import com.company.newseat.global.util.DateUtil;
import com.company.newseat.news.domain.News;
import org.springframework.web.util.HtmlUtils;

public record CategoryNewsResponse(
        Long newsId,
        String title,
        String imgUrl,
        String publisher,
        String publishedAt
) {
    public static CategoryNewsResponse from (News news) {
        String formattedPublishedAt = DateUtil.formatDate(news.getPublished_at());

        return new CategoryNewsResponse(
                news.getNewsId(),
                HtmlUtils.htmlUnescape(news.getTitle()),
                news.getImgUrl(),
                news.getPublisher(),
                formattedPublishedAt
        );
    }
}
