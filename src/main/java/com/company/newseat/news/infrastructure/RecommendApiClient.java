package com.company.newseat.news.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecommendApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.recommend.url}")
    private String flaskApiUrl1;

    @Value("${flask.recommendlist.url}")
    private String flaskApiUrl2;

    public List<Long> getRecommendedIds(Long newsId) {
        Map<String, Object> requestBody = Map.of("news_id", newsId);

        Map<String, Object> response = restTemplate.postForObject(
                flaskApiUrl1,
                requestBody,
                Map.class
        );

        List<Integer> recommendedIds = (List<Integer>) response.getOrDefault("recommended_ids", List.of());
        return recommendedIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public List<Long> getRecommendedIdsForUser(List<Long> inputIds) {
        Map<String, Object> requestBody = Map.of("news_ids", inputIds.toArray(new Long[0]));

        Map<String, Object> response = restTemplate.postForObject(
                flaskApiUrl2,
                requestBody,
                Map.class
        );

        List<Integer> recommendedIds = (List<Integer>) response.getOrDefault("recommended_ids", List.of());
        return recommendedIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
