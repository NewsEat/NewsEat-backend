package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.global.util.DateUtil;
import org.springframework.web.util.HtmlUtils;

public record BookmarkSimpleResponse(
        Long bookmarkId,
        String title,
        String category,
        String imgUrl,
        String publishedAt,
        boolean newsDeleted
) {
    public static BookmarkSimpleResponse from (Bookmark bookmark, boolean newsDeleted) {
        String formattedPublishedAt = DateUtil.formatDate(bookmark.getPublished_at());

        return new BookmarkSimpleResponse(
                bookmark.getBookmarkId(),
                HtmlUtils.htmlUnescape(bookmark.getTitle()),
                bookmark.getCategory(),
                bookmark.getImgUrl(),
                formattedPublishedAt,
                newsDeleted
        );
    }
}
