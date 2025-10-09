package com.company.newseat.news.dto.response;

public record NaverNews(
        String title,
        String description,
        String url,
        String pubDate
) {
}
