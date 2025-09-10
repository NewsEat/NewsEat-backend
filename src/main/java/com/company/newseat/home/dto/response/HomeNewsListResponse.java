package com.company.newseat.home.dto.response;

import java.util.List;

public record HomeNewsListResponse (
        List<HomeNewsResponse> homeNewsResponses
) {
    public static HomeNewsListResponse of (List<HomeNewsResponse>  homeNewsResponses){
        return new HomeNewsListResponse(homeNewsResponses);
    }
}
