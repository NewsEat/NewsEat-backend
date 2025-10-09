package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.infrastructure.UnsplashApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Unsplash Image API Test", description = "Unsplash 이미지 검색 API 연동 테스트")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/unsplash")
public class UnsplashTestController {

    private final UnsplashApiClient unsplashApiClient;

    @Operation(summary = "이미지 검색 API 호출", description = "키워드 기반 Unsplash API 호출 테스트")
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getImage(@RequestParam String keyword) {
        String imageUrl = unsplashApiClient.getImageUrl(keyword);
        if (imageUrl == null){
            return ResponseEntity.ok(ApiResponse.onFailure("404", "이미지를 찾을 수 없습니다.", null));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(imageUrl));
    }

}
