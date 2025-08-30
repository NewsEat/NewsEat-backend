package com.company.newseat.news.dto.response;

import java.util.List;

public record CategoryNewsListResponse(
        List<CategoryNewsResponse> categoryNewsResponses,
        boolean hasNext
) {
    public static CategoryNewsListResponse of (List<CategoryNewsResponse> categoryNewsResponses, boolean hasNext) {
        return new CategoryNewsListResponse(categoryNewsResponses, hasNext);
    }
}