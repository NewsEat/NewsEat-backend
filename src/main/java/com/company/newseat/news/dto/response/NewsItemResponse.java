package com.company.newseat.news.dto.response;

import com.company.newseat.news.domain.News;
import org.springframework.web.util.HtmlUtils;

public record NewsItemResponse (
        Long newsId,
        String imgUrl,
        String title
) {
    public static NewsItemResponse from(News news) {
        return new NewsItemResponse(
                news.getNewsId(),
                news.getImgUrl(),
                HtmlUtils.htmlUnescape(news.getTitle())
        );
    }
}
