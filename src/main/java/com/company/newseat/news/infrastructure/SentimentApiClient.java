package com.company.newseat.news.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class SentimentApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    public String analyzeSentiment(Long newsId, String title, String content) {
        try {
            Map<String, Object> request = Map.of(
                    "news_id", newsId,
                    "title", title,
                    "content", content
            );

            ResponseEntity<Map<String, String>> response =
                    restTemplate.postForEntity(flaskApiUrl, request, (Class<Map<String, String>>)(Class<?>) Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().get("sentiment");
            }
        } catch (Exception e) {
            log.warn("Sentiment API 호출 실패 - newsId: {}, error: {}", newsId, e.getMessage());
        }
        return null;
    }
}
