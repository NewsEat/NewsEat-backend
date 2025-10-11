package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.global.util.DateUtil;
import com.company.newseat.news.domain.type.Sentiment;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

public record BookmarkResponse(
        Long bookmarkId,
        String title,
        String content,
        String publisher,
        String sentiment,
        String publishedAt,
        String imgUrl,
        String category,
        boolean newsDeleted
) {
    public static BookmarkResponse from(Bookmark bookmark, boolean newsDeleted) {
        String formattedPublishedAt = DateUtil.formatDate(bookmark.getPublished_at());

        String sentiment = Optional.ofNullable(bookmark.getSentiment())
                .map(Sentiment::getDescription)
                .orElse("감정 정보 없음");

        String category = Optional.ofNullable(bookmark.getCategory())
                .orElse("카테고리 없음");

        return new BookmarkResponse(
                bookmark.getBookmarkId(),
                HtmlUtils.htmlUnescape(bookmark.getTitle()),
                HtmlUtils.htmlUnescape(bookmark.getContent()),
                bookmark.getPublisher(),
                sentiment,
                formattedPublishedAt,
                bookmark.getImgUrl(),
                category,
                newsDeleted
        );
    }
}
