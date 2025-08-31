package com.company.newseat.bookmark.dto.response;

import java.util.List;

public record BookmarkSimpleListResponse(
        List<BookmarkSimpleResponse> bookmarkResponseList,
        boolean hasNext
) {
    public static BookmarkSimpleListResponse of (List<BookmarkSimpleResponse> bookmarkResponseList, boolean hasNext) {
        return new BookmarkSimpleListResponse(bookmarkResponseList, hasNext);
    }
}
