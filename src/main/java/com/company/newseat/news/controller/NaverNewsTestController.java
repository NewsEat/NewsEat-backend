package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.infrastructure.NaverNewsApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Naver News API Test", description = "네이버 뉴스 검색 API 연동 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/naver-news")
public class NaverNewsTestController {

    private final NaverNewsApiClient naverNewsApiClient;

    @Operation(summary = "네이버 뉴스 검색 API 호출", description = "키워드 기반으로 네이버 뉴스 검색 API 호출 테스트")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getNews(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "1") int start) {
        Object newsResponse = naverNewsApiClient.getNewsList(keyword, count, start);
        if (newsResponse == null) {
            return ResponseEntity.ok(ApiResponse.onFailure("404", "뉴스를 찾을 수 없습니다.", null));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(newsResponse));
    }
}
