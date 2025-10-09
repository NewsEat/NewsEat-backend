package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.application.NewsSaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "News Save API Test", description = "뉴스 저장 API 테스트")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/save")
public class NewsSaveTestController {

    private final NewsSaveService newsService;

    /**
     * 스케줄러 없이 직접 뉴스 저장 로직 실행
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> fetchNewsManually(
            @RequestParam(defaultValue = "1") int count) {

        LocalDateTime startTime = LocalDateTime.now();
        log.info("뉴스 수동 실행 시작: {}, days={}", startTime, count);

        try {
            newsService.fetchAndSaveDailyNews(count);
            LocalDateTime endTime = LocalDateTime.now();
            log.info("뉴스 수동 실행 종료: {}", endTime);

            return ResponseEntity.ok(
                    ApiResponse.onSuccess("뉴스 수동 실행 완료 (" + startTime + " ~ " + endTime + ")")
            );

        } catch (Exception e) {
            log.error("뉴스 수동 실행 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.onFailure("500", "뉴스 수동 실행 실패: " + e.getMessage(), null));
        }
    }
}
