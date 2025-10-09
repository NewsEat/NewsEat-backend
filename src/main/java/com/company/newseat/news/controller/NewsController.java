package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.application.NewsRecommendService;
import com.company.newseat.news.application.NewsService;
import com.company.newseat.news.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "News", description = "뉴스 조회, 요약 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;
    private final NewsRecommendService newsRecommendService;

    @Operation(summary = "뉴스 요약", description = "뉴스 ID를 받아 AI를 활용해 뉴스 요약 생성")
    @PostMapping("/summary/{newsId}")
    public ResponseEntity<ApiResponse<NewsSummaryResponse>> summarizeNewsById(
            @PathVariable Long newsId) {

        NewsSummaryResponse response = newsService.summarizeNewsById(newsId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "뉴스 검색 (커서 기반 무한 스크롤)",
            description = "키워드를 기준으로 뉴스 제목과 내용을 검색, lastNewsId를 기준으로 다음 페이지 데이터 조회")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<SearchNewsListResponse>> searchNews(
            @RequestParam String keyword,
            @RequestParam(required = false) Long lastNewsId,
            @RequestParam(defaultValue = "10") int size) {

        SearchNewsListResponse response = newsService.searchNews(keyword, lastNewsId, size);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @Operation(summary = "카테고리별 뉴스 목록 조회",
            description = "카테고리 코드를 기반으로 카테고리 뉴스 목록 조회 <br>" +
            "카테고리 코드: 정치(001), 경제(002), 사회(003), 생활/문화(004), IT/과학(005), 연예(006), 스포츠(007),세계(008)")
    @GetMapping
    public ResponseEntity<ApiResponse<CategoryNewsListResponse>> getNewsByCategory(
            @RequestParam(defaultValue = "001") String category,
            @RequestParam(required = false) Long lastNewsId,
            @RequestParam(defaultValue = "10") int size){

        CategoryNewsListResponse response = newsService.getCategoryNews(category, lastNewsId, size);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "뉴스 상세 조회", description = "뉴스 ID를 기반으로 뉴스 상세를 조회")
    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsDetailResponse>> getNewsDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long newsId){

        NewsDetailResponse response = newsService.getNewsDetail(userId, newsId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "뉴스 하단 제안 뉴스 조회", description = "뉴스 카테고리와 일치하는 제안 뉴스 5개 조회")
    @GetMapping("/{newsId}/recommendations")
    public ResponseEntity<ApiResponse<SuggestedNewsListResponse>> getSuggestedNews(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long newsId){

        SuggestedNewsListResponse response = newsService.getSuggestedNews(newsId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "뉴스 하단 제안 뉴스 조회", description = "제안 뉴스 5개 조회 (FLASK API 연동)")
    @GetMapping("/{newsId}/recommendations/v2")
    public ResponseEntity<ApiResponse<SuggestedNewsListResponse>> getSuggestedNewsV2(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long newsId){

        SuggestedNewsListResponse response = newsRecommendService.getSuggestedNewsFromFlask(newsId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
