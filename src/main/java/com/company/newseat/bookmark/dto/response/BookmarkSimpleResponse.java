package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;

public record BookmarkSimpleResponse(
        Long bookmarkId,
        String title,
        String category,
        String imgUrl,
        String publishedAt
) {
    public static BookmarkSimpleResponse of(Bookmark bookmark) {
        return new BookmarkSimpleResponse(
                bookmark.getBookmarkId(),
                bookmark.getTitle(),
                bookmark.getCategory(),
                bookmark.getImgUrl(),
                bookmark.getPublished_at()
        );
    }
}
