package com.company.newseat.news.infrastructure;

import com.company.newseat.news.application.NewsSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsScheduler {

    private final NewsSaveService newsService;

    @Scheduled(cron = "0 00 02 * * ?")
    public void fetchDailyNews() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("뉴스 스케줄러 시작: {}", startTime);

        try {
            newsService.fetchAndSaveDailyNews(2);
        } catch (Exception e) {
            log.error("뉴스 스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
        }

        LocalDateTime endTime = LocalDateTime.now();
        log.info("뉴스 스케줄러 종료: {}", endTime);
    }
}