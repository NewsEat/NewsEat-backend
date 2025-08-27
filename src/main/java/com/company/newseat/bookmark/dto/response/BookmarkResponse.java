package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;

public record BookmarkResponse(
        Long bookmarkId,
        String title,
        String content,
        String publisher,
        String sentiment,
        String publishedAt,
        String imgUrl,
        String category
) {
    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getBookmarkId(),
                bookmark.getTitle(),
                bookmark.getContent(),
                bookmark.getPublisher(),
                bookmark.getSentiment().getDescription(),
                bookmark.getPublished_at(),
                bookmark.getImgUrl(),
                bookmark.getCategory());
    }
}
