package com.company.newseat.home.presentation;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.home.application.HomeService;
import com.company.newseat.home.dto.response.PositiveNewsListResponse;
import com.company.newseat.user.application.UserService;
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

    @Operation(summary = "홈 화면 긍정 뉴스 조회", description = "사용자 선호 카테고리 + 전체 카테고리에서 긍정 뉴스 5개 조회")
    @GetMapping("/positive-news")
    public ResponseEntity<ApiResponse<PositiveNewsListResponse>> getHomePositiveNews(
            @AuthenticationPrincipal Long userId) {

        PositiveNewsListResponse response = homeService.getHomePositiveNews(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
