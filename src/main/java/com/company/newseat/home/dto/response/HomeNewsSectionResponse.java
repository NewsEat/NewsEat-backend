package com.company.newseat.home.dto.response;

import com.company.newseat.news.dto.response.NewsItemResponse;

import java.util.List;

public record HomeNewsSectionResponse(
        boolean isDetox,
        List<Section> sections
) {
    public record Section(
            String type,       // "positiveNews", "prefCategoryNews", //"latestNews"
            String title,
            List<NewsItemResponse> newsList
    ) {}
}