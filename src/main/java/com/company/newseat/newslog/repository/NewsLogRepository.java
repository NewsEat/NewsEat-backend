package com.company.newseat.newslog.repository;

import com.company.newseat.news.domain.News;
import com.company.newseat.newslog.domain.NewsLog;
import com.company.newseat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsLogRepository extends JpaRepository<NewsLog, Long> {

//    @Query("SELECT nl.news FROM NewsLog nl WHERE nl.user = :user ORDER BY nl.createdDate DESC")
//    List<News> findTop5NewsByUser(@Param("user") User user);

    @Query("SELECT nl.news.newsId FROM NewsLog nl WHERE nl.user.userId = :userId ORDER BY nl.createdDate DESC LIMIT :limit")
    List<Long> findRecentNewsIdsByUserId(@Param("userId") Long userId, @Param("limit") int limit);

}
