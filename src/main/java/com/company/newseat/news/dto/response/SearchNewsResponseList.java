package com.company.newseat.news.dto.response;

import java.util.List;

public record SearchNewsResponseList(
        List<SearchNewsResponse> newsResponseList,
        boolean hasNext
) {
    public static SearchNewsResponseList of (List<SearchNewsResponse> newsResponseList, boolean hasNext) {
        return new SearchNewsResponseList(newsResponseList, hasNext);
    }
}
