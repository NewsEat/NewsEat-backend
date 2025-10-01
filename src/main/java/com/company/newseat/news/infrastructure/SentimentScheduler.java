package com.company.newseat.news.infrastructure;

import com.company.newseat.news.application.SentimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SentimentScheduler {

    private final SentimentService sentimentService;

    /*
    @Scheduled(cron = "0 00 04 * * ?")
    public void runUpdate() {
        try {
        sentimentService.updateEmptySentiments();
        } catch (Exception e) {
            log.error("Sentiment 업데이트 중 오류 발생: {}", e.getMessage());
        }
    }
     */
}
