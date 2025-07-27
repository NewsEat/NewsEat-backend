package com.company.newseat.newslog.repository;

import com.company.newseat.newslog.domain.NewsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLogRepository extends JpaRepository<NewsLog, Long> {
}
