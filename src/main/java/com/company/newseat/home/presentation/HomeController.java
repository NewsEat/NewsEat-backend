package com.company.newseat.home.presentation;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.home.application.HomeService;
import com.company.newseat.home.dto.response.HomeNewsListResponse;
import com.company.newseat.home.dto.response.HomeNewsSectionResponse;
import com.company.newseat.home.dto.response.HomePreferNewsListResponse;
import com.company.newseat.home.dto.response.PositiveNewsListResponse;
import com.company.newseat.news.application.NewsRecommendService;
import com.company.newseat.news.dto.response.SuggestedNewsListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home", description = "홈 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;
    private final NewsRecommendService newsRecommendService;

    @Operation(summary = "홈 화면 긍정 뉴스 섹션 조회", description = "사용자 선호 카테고리 + 전체 카테고리에서 긍정 뉴스 5개 조회")
    @GetMapping("/positive-news")
    public ResponseEntity<ApiResponse<PositiveNewsListResponse>> getHomePositiveNews(
            @AuthenticationPrincipal Long userId) {

        PositiveNewsListResponse response = homeService.getHomePositiveNews(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "홈 화면 관심 카테고리 뉴스 섹션 조회", description = "사용자 선호 카테고리별 뉴스 5개씩 조회")
    @GetMapping("/preference-news")
    public ResponseEntity<ApiResponse<HomePreferNewsListResponse>> getHomePrefNewsByCategory(
            @AuthenticationPrincipal Long userId) {

        HomePreferNewsListResponse response = homeService.getHomePrefNewsByCategory(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "홈 화면 최신 뉴스 조회", description = "최신 뉴스 5개 조회")
    @GetMapping("/latest-news")
    public ResponseEntity<ApiResponse<HomeNewsListResponse>> getHomeNews(
            @AuthenticationPrincipal Long userId) {

        HomeNewsListResponse response = homeService.getHomeNews(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "홈 화면 통합 뉴스 조회", description = "긍정 뉴스, 관심 카테고리 뉴스, 최신 뉴스 섹션 조회")
    @GetMapping("/news-sections")
    public ResponseEntity<ApiResponse<HomeNewsSectionResponse>> getHomeNewsSections(
            @AuthenticationPrincipal Long userId) {

        HomeNewsSectionResponse response = homeService.getHomeNewsSections(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

//    @Operation(summary = "홈 화면 추천 뉴스 조회", description = "유저의 최근 뉴스 로그를 기반으로 Flask 추천 API 호출 후 추천 뉴스 5개 반환 (로그 없을 시 최신 뉴스 5개)")
//    @GetMapping("/v2/latest-news")
//    public ResponseEntity<ApiResponse<SuggestedNewsListResponse>> getHomeRecommendedNews(
//            @AuthenticationPrincipal Long userId) {
//
//        SuggestedNewsListResponse response = newsRecommendService.getHomeRecommendedNews(userId);
//
//        return ResponseEntity.ok(ApiResponse.onSuccess(response));
//    }
}
