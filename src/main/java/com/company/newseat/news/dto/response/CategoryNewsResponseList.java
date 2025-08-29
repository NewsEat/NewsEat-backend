package com.company.newseat.news.dto.response;

import java.util.List;

public record CategoryNewsResponseList(
        List<CategoryNewsResponse> categoryNewsResponses,
        boolean hasNext
) {
    public static CategoryNewsResponseList of (List<CategoryNewsResponse> categoryNewsResponses, boolean hasNext) {
        return new CategoryNewsResponseList(categoryNewsResponses, hasNext);
    }
}