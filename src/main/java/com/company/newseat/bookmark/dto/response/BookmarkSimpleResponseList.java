package com.company.newseat.bookmark.dto.response;

import java.util.List;

public record BookmarkSimpleResponseList(
        List<BookmarkSimpleResponse> bookmarkResponseList,
        boolean hasNext
) {
    public static BookmarkSimpleResponseList of (List<BookmarkSimpleResponse> bookmarkResponseList, boolean hasNext) {
        return new BookmarkSimpleResponseList(bookmarkResponseList, hasNext);
    }
}
