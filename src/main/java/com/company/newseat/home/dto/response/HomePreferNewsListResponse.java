package com.company.newseat.home.dto.response;

import java.util.List;

public record HomePreferNewsListResponse (
        List<HomeCategoryNews> homeCategoryNews
) {
    public static HomePreferNewsListResponse of (List<HomeCategoryNews> homeCategoryNews){
        return new HomePreferNewsListResponse(homeCategoryNews);
    }

    public record HomeCategoryNews(
            String categoryName,
            List<HomePreferNewsResponse> newsList
    ) {
    }
}
