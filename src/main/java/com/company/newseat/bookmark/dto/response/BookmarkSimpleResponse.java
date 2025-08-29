package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.global.util.DateUtil;

public record BookmarkSimpleResponse(
        Long bookmarkId,
        String title,
        String category,
        String imgUrl,
        String publishedAt
) {
    public static BookmarkSimpleResponse of(Bookmark bookmark) {
        String formattedPublishedAt = DateUtil.formatDate(bookmark.getPublished_at());

        return new BookmarkSimpleResponse(
                bookmark.getBookmarkId(),
                bookmark.getTitle(),
                bookmark.getCategory(),
                bookmark.getImgUrl(),
                formattedPublishedAt
        );
    }
}
