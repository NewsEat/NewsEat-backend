package com.company.newseat.home.dto.response;

import java.util.List;

public record HomeNewsListResponse (
        List<NewsItemResponse> homeNewsResponses
) {
    public static HomeNewsListResponse of (List<NewsItemResponse>  homeNewsResponses){
        return new HomeNewsListResponse(homeNewsResponses);
    }
}
