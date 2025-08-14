package com.company.newseat.news.repository;

import com.company.newseat.news.domain.News;

import java.util.List;

public interface NewsRepositoryCustom {

    List<News> searchByKeywordWithCursor(String keyword, Long lastNewsId, int size);

}
