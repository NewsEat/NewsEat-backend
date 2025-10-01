package com.company.newseat.news.controller;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.news.dto.request.SentimentAnalysisRequest;
import com.company.newseat.news.dto.response.SentimentAnalysisResponse;
import com.company.newseat.news.infrastructure.SentimentApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Flask Sentiment analysis API", description = "감정 분석 API 연동 테스트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/sentiment-analysis")
public class SentimentAnalysisTestController {

    private final SentimentApiClient sentimentApiClient;

    @Operation(summary = "단건 감정 분석 테스트", description = "개별 뉴스에 대한 감정 분석 테스트")
    @PostMapping
    public ResponseEntity<ApiResponse<SentimentAnalysisResponse>> testSentimentAnalysis(
            @RequestBody SentimentAnalysisRequest request) {
        try {
            String sentiment = sentimentApiClient.analyzeSentiment(
                    request.news_id(),
                    request.title(),
                    request.content()
            );

            if (sentiment != null) {
                SentimentAnalysisResponse response =
                        new SentimentAnalysisResponse(request.news_id(), sentiment);
                return ResponseEntity.ok(ApiResponse.onSuccess(response));
            } else {
                SentimentAnalysisResponse errorResponse =
                        new SentimentAnalysisResponse(request.news_id(), "API 호출 실패");
                return ResponseEntity.status(500).body(ApiResponse.onFailure("500", "Flask API 호출 실패", errorResponse));
            }
        } catch (Exception e) {
            SentimentAnalysisResponse errorResponse =
                    new SentimentAnalysisResponse(request.news_id(), "예외 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.onFailure("500", e.getMessage(), errorResponse));
        }
    }
}
