package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.global.util.DateUtil;

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
        String formattedPublishedAt = DateUtil.formatDate(bookmark.getPublished_at());

        return new BookmarkResponse(
                bookmark.getBookmarkId(),
                bookmark.getTitle(),
                bookmark.getContent(),
                bookmark.getPublisher(),
                bookmark.getSentiment().getDescription(),
                formattedPublishedAt,
                bookmark.getImgUrl(),
                bookmark.getCategory());
    }
}
