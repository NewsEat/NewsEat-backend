package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.infrastructure.RecommendApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Flask Recommend API Test", description = "추천 뉴스 API 연동 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/recommend")
public class RecommendNewsTestController {

    private final RecommendApiClient recommendApiClient;

    @Operation(summary = "하단 뉴스 추천 테스트", description = "Flask 추천 API 호출 후 추천 뉴스 ID 목록 반환")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Long>>> getRecommendedNews(@RequestParam Long newsId) {
        try {
            log.info("추천 뉴스 요청 - newsId: {}", newsId);

            List<Long> recommendedIds = recommendApiClient.getRecommendedIds(newsId);

            if (recommendedIds.isEmpty()) {
                log.warn("Flask API로부터 추천 뉴스가 없습니다. newsId={}", newsId);
                return ResponseEntity.ok(ApiResponse.onFailure("404", "추천 결과가 없습니다.", List.of()));
            }

            log.info("추천 결과: {}", recommendedIds);
            return ResponseEntity.ok(ApiResponse.onSuccess(recommendedIds));

        } catch (Exception e) {
            log.error("추천 뉴스 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("500", "Flask API 호출 실패: " + e.getMessage(), List.of()));
        }
    }
}
