package com.company.newseat.home.dto.response;

import com.company.newseat.news.dto.response.NewsItemResponse;

import java.util.List;

public record PositiveNewsListResponse (
        List<NewsItemResponse>  positiveNewsResponses
) {
    public static PositiveNewsListResponse of (List<NewsItemResponse>  positiveNewsResponses){
        return new PositiveNewsListResponse(positiveNewsResponses);
    }
}
