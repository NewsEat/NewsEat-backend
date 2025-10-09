package com.company.newseat.news.infrastructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class UnsplashApiClient {

    @Value("${unsplash.access.key}")
    private String accessKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 키워드 기반으로 Unsplash에서 이미지 URL 1개 가져오기
     */
    public String getImageUrl(String keyword) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.unsplash.com/search/photos")
                .queryParam("query", keyword)
                .queryParam("per_page", 1)
                .queryParam("client_id", accessKey)
                .toUriString();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
        if (results == null || results.isEmpty()) return null;

        Map<String, Object> urls = (Map<String, Object>) results.get(0).get("urls");
        return urls.get("regular").toString();
    }
}
