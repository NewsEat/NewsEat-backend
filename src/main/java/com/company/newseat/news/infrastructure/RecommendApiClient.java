package com.company.newseat.news.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RecommendApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.recommend.url}")
    private String flaskApiUrl;

    public List<Long> getRecommendedIds(Long newsId) {
        Map<String, Object> requestBody = Map.of(
                "newsId", newsId
        );

        List<Long> recommendedIds = restTemplate.postForObject(
                flaskApiUrl,
                requestBody,
                List.class
        );

        return recommendedIds != null ? recommendedIds : List.of();
    }

    public List<Long> getRecommendedIdsForUser(List<Long> recentNewsIds) {
        Map<String, Object> requestBody = Map.of("recentNewsIds", recentNewsIds);
        List<Long> recommendedIds = restTemplate.postForObject(
                flaskApiUrl, requestBody, List.class
        );
        return recommendedIds != null ? recommendedIds : List.of();
    }
}
