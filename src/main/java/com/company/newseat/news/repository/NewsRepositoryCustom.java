package com.company.newseat.news.repository;

import com.company.newseat.home.dto.response.HomePreferNewsResponse;
import com.company.newseat.home.dto.response.PositiveNewsResponse;
import com.company.newseat.news.domain.News;

import java.util.List;

public interface NewsRepositoryCustom {

    List<News> searchByKeywordWithCursor(String keyword, Long lastNewsId, int size);
    List<News> findByCategoryWithCursor(String categoryCode, Long lastNewsId, int size);


    List<PositiveNewsResponse> findGlobalPositiveNews(int limit);
    List<PositiveNewsResponse> findPreferredPositiveNews(List<Long> categoryIds, int limit);

    List<HomePreferNewsResponse> findPreferredPositiveNews(Long categoryId, int limit);
    List<HomePreferNewsResponse> findByCategoryWithLimit(Long categoryId, int limit);
}
