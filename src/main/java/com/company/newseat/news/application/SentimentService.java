package com.company.newseat.news.application;

import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.news.infrastructure.SentimentApiClient;
import com.company.newseat.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {

    private final NewsRepository newsRepository;
    private final SentimentApiClient sentimentApiClient;

    public void updateEmptySentiments() {
        List<News> newsList = newsRepository.findBySentimentIsNull(PageRequest.of(0, 50));

        for (News news : newsList) {
            try {
                String sentimentStr = sentimentApiClient.analyzeSentiment(
                        news.getNewsId(),
                        news.getTitle(),
                        news.getContent()
                );

                if (sentimentStr != null) {
                    Sentiment sentiment = Sentiment.fromString(sentimentStr);
                    news.updateSentiment(sentiment);
                    newsRepository.save(news);
                }
            } catch (Exception e) {
                log.warn("뉴스 ID [{}] 감정 분석 실패: {}", news.getNewsId(), e.getMessage(), e);
            }
        }
    }
}
