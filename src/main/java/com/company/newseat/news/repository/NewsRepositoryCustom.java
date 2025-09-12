package com.company.newseat.news.repository;

import com.company.newseat.news.dto.response.NewsItemResponse;
import com.company.newseat.news.domain.News;

import java.util.List;

public interface NewsRepositoryCustom {

    List<News> searchByKeywordWithCursor(String keyword, Long lastNewsId, int size);
    List<News> findByCategoryWithCursor(String categoryCode, Long lastNewsId, int size);

    List<NewsItemResponse> findGlobalPositiveNews(int limit);
    List<NewsItemResponse> findPreferredPositiveNews(List<Long> categoryIds, int limit);

    List<NewsItemResponse> findPreferredPositiveNews(Long categoryId, int limit);
    List<NewsItemResponse> findByCategoryWithLimit(Long categoryId, int limit);

    List<NewsItemResponse> findLatestNews(int limit);
    List<NewsItemResponse> findLatestPositiveNews(int limit);
}
