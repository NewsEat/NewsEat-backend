package com.company.newseat.news.infrastructure;

import com.company.newseat.news.application.NewsSaveService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NewsScheduler {

    private final NewsSaveService newsService;

    public NewsScheduler(NewsSaveService newsService) {
        this.newsService = newsService;
    }

    @Scheduled(cron = "0 00 02 * * ?")
    public void fetchDailyNews() {
        System.out.println("뉴스 스케줄러 시작: " + LocalDateTime.now());
        newsService.fetchAndSaveDailyNews(1);
        System.out.println("뉴스 스케줄러 종료: " + LocalDateTime.now());
    }
}