package com.company.newseat.news.dto.response;

import java.util.List;

public record SuggestedNewsListResponse(
        List<NewsItemResponse> suggestedNewsResponses
) {
    public static SuggestedNewsListResponse of (List<NewsItemResponse> suggestedNewsResponses){
        return new SuggestedNewsListResponse(suggestedNewsResponses);
    }
}
