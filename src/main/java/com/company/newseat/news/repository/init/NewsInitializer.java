package com.company.newseat.news.repository.init;

import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.global.util.DataInit;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DataInit
@Order(4)
public class NewsInitializer implements ApplicationRunner {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (newsRepository.count() > 0) {
            log.info("[News] 뉴스 더미 데이터 존재");
        } else {
            List<News> newsList = new ArrayList<>();

            for (long categoryId = 1; categoryId <= 9; categoryId++) {
                Category category = categoryRepository.findById(categoryId)
                        .orElse(null);

                if (category == null) {
                    log.warn("[NewsInitializer] categoryId {} 는 존재하지 않음", categoryId);
                    continue;
                }

                for (int i = 1; i <= 6; i++) {
                    News news = News.builder()
                            .title(category.getName() + " 뉴스 제목 " + i)
                            .content(category.getName() + " 카테고리의 뉴스 내용입니다. (" + i + ")")
                            .publisher("뉴스발행사 " + (char) ('A' + i - 1))
                            .sentiment(i % 2 == 0 ? Sentiment.POSITIVE : Sentiment.NEGATIVE)
                            .published_at("2025-08-08 10:0" + i + ":00")
                            .imgUrl("https://raw.githubusercontent.com/NewsEat/assets/refs/heads/main/news_eat_logo.png")
                            .category(category)
                            .build();
                    newsList.add(news);
                }
            }
            newsRepository.saveAll(newsList);
        }
    }
}
