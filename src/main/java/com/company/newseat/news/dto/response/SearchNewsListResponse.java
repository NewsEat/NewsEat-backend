package com.company.newseat.news.dto.response;

import java.util.List;

public record SearchNewsListResponse(
        List<SearchNewsResponse> searchNewsResponses,
        boolean hasNext
) {
    public static SearchNewsListResponse of (List<SearchNewsResponse> searchNewsResponses, boolean hasNext) {
        return new SearchNewsListResponse(searchNewsResponses, hasNext);
    }
}
