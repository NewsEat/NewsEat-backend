package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.application.NewsService;
import com.company.newseat.news.dto.request.NewsSummaryRequest;
import com.company.newseat.news.dto.response.NewsSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "News", description = "뉴스 조회, 요약 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 요약", description = "뉴스 ID를 받아 AI를 활용해 뉴스 요약 생성")
    @PostMapping("/summary/{newsId}")
    public ResponseEntity<ApiResponse<NewsSummaryResponse>> summarizeNewsById(
            @PathVariable Long newsId) {

        NewsSummaryResponse response = newsService.summarizeNewsById(newsId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "뉴스 요약", description = "뉴스 본문 내용을 받아 AI를 활용해 뉴스 요약 생성")
    @PostMapping("/summary")
    public ResponseEntity<ApiResponse<NewsSummaryResponse>> summarizeNewsByContent(
            @RequestBody NewsSummaryRequest request) {

        NewsSummaryResponse response = newsService.summarizeNewsByContent(request);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
