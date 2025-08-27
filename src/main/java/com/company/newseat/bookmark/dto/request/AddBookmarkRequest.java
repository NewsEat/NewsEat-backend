package com.company.newseat.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddBookmarkRequest(
        @NotNull
        Long newsId
) {
}
