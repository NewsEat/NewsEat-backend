package com.company.newseat.bookmark.presentation;

import com.company.newseat.bookmark.application.BookmarkService;
import com.company.newseat.bookmark.dto.response.AddBookmarkResponse;
import com.company.newseat.bookmark.dto.response.BookmarkResponse;
import com.company.newseat.bookmark.dto.response.BookmarkSimpleListResponse;
import com.company.newseat.bookmark.dto.response.BookmarkSummaryResponse;
import com.company.newseat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<ApiResponse<AddBookmarkResponse>> createBookmark(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long newsId) {

        Long bookmarkId = bookmarkService.addBookmark(userId, newsId);

        AddBookmarkResponse response = new AddBookmarkResponse(bookmarkId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "북마크 삭제", description = "뉴스를 북마크에서 취소")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long bookmarkId) {

        bookmarkService.deleteBookmark(userId, bookmarkId);

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "북마크 단건 조회", description = "사용자가 북마크한 뉴스 단건 조회")
    @GetMapping("/{bookmarkId}")
    public ResponseEntity<ApiResponse<BookmarkResponse>> getBookmark(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long bookmarkId) {

        BookmarkResponse response = bookmarkService.getBookmark(userId, bookmarkId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "북마크 리스트 조회 (커서 기반 무한 스크롤)",
            description = "lastBookmarkId를 기준으로 다음 페이지 데이터 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<BookmarkSimpleListResponse>> getBookmarkList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Long lastBookmarkId,
            @RequestParam(defaultValue = "10") int size) {

        BookmarkSimpleListResponse response = bookmarkService.getBookmarkList(userId, lastBookmarkId, size);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "북마크한 뉴스 내용 요약", description = "bookmarkId ID를 받아 AI를 활용해 북마크한 뉴스 내용 요약 생성")
    @PostMapping("/summary/{bookmarkId}")
    public ResponseEntity<ApiResponse<BookmarkSummaryResponse>> summarizeBookmark(
            @PathVariable Long bookmarkId) {

        BookmarkSummaryResponse response = bookmarkService.summarizeBookmark(bookmarkId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
