package com.company.newseat.bookmark.dto.response;

import com.company.newseat.bookmark.domain.Bookmark;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record BookmarkSimpleResponse(
        Long bookmarkId,
        String title,
        String category,
        String imgUrl,
        String publishedAt
) {
    public static BookmarkSimpleResponse of(Bookmark bookmark) {
        String formattedPublishedAt = formatDate(bookmark.getPublished_at());

        return new BookmarkSimpleResponse(
                bookmark.getBookmarkId(),
                bookmark.getTitle(),
                bookmark.getCategory(),
                bookmark.getImgUrl(),
                formattedPublishedAt
        );
    }

    private static String formatDate(String rawDate) {

        if (rawDate == null || rawDate.isBlank()) {
            return "";
        }

        DateTimeFormatter[] inputFormatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        };

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        for (DateTimeFormatter inputFormatter : inputFormatters) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(rawDate, inputFormatter);
                return dateTime.format(outputFormatter);
            } catch (Exception e1) {
                try {
                    LocalDate date = LocalDate.parse(rawDate, inputFormatter);
                    return date.format(outputFormatter);
                } catch (Exception e2) {
                }
            }
        }
        return rawDate;
    }
}
