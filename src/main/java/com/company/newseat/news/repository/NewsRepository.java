package com.company.newseat.news.repository;

import com.company.newseat.news.domain.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {

    @Query("SELECT n FROM News n JOIN FETCH n.category WHERE n.newsId = :newsId")
    Optional<News> findByIdWithCategory(@Param("newsId") Long newsId);

    boolean existsByTitle(String title);

    List<News> findBySentimentIsNull(Pageable pageable);

    @Query(value = "SELECT * FROM news WHERE news_id NOT IN :excludedIds ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<News> findRandomNewsExcludingIds(@Param("excludedIds") List<Long> excludedIds, @Param("limit") int limit);
}
