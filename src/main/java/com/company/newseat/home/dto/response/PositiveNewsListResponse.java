package com.company.newseat.home.dto.response;

import java.util.List;

public record PositiveNewsListResponse (
        List<PositiveNewsResponse>  positiveNewsResponses
) {
    public static PositiveNewsListResponse of (List<PositiveNewsResponse>  positiveNewsResponses){
        return new PositiveNewsListResponse(positiveNewsResponses);
    }
}
