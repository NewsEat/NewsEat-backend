package com.company.newseat.home.dto.response;

import com.company.newseat.news.dto.response.NewsItemResponse;

import java.util.List;

public record HomePreferNewsListResponse (
        List<HomeCategoryNews> homeCategoryNews
) {
    public static HomePreferNewsListResponse of (List<HomeCategoryNews> homeCategoryNews){
        return new HomePreferNewsListResponse(homeCategoryNews);
    }

    public record HomeCategoryNews(
            String categoryName,
            List<NewsItemResponse> newsList
    ) {
    }
}
