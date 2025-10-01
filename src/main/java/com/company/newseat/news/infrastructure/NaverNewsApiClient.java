package com.company.newseat.news.infrastructure;

import com.company.newseat.news.dto.response.NaverNews;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class NaverNewsApiClient {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 네이버 뉴스 검색 API 에서 뉴스 내용 불러오기
     */
    public List<NaverNews> getNewsList(String categoryQuery, int count, int start) {
        String url = UriComponentsBuilder.fromHttpUrl("https://openapi.naver.com/v1/search/news.json")
                .queryParam("query", categoryQuery)
                .queryParam("display", count)
                .queryParam("start", start)
                .queryParam("sort", "date")
                .toUriString();

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

       ResponseEntity<Map> response = restTemplate.exchange(url,
                org.springframework.http.HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

        return items.stream().map(item -> new NaverNews(
                item.get("title").toString().replaceAll("<.*?>", ""),
                item.get("description").toString().replaceAll("<.*?>", ""),
                item.get("link").toString(),
                item.get("pubDate").toString()
        )).toList();
    }
}
