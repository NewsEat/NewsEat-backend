package com.company.newseat.bookmark.presentation;

import com.company.newseat.bookmark.application.BookmarkService;
import com.company.newseat.bookmark.dto.request.AddBookmarkRequest;
import com.company.newseat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bookmark", description = "북마크 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 추가", description = "뉴스를 북마크에 추가")
    @PostMapping("/{newsId}")
    public ResponseEntity<ApiResponse<Void>> createBookmark(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddBookmarkRequest request) {

        bookmarkService.addBookmark(userId, request.newsId());

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
