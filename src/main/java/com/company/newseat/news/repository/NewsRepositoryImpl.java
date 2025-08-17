package com.company.newseat.news.repository;

import com.company.newseat.news.domain.News;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.company.newseat.news.domain.QNews.news;

@RequiredArgsConstructor
@Repository
public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<News> searchByKeywordWithCursor(String keyword, Long lastNewsId, int size) {
        String lowerKeyword = keyword.toLowerCase();

        var query = queryFactory.selectFrom(news)
                .where(
                        Expressions.stringTemplate(
                                "lower(cast({0} as char))", news.content)
                                .like("%" + lowerKeyword + "%")
                                .or(
                       Expressions.stringTemplate(
                               "lower(cast({0} as char))", news.title)
                               .like("%" + lowerKeyword + "%"))
                )
                .orderBy(news.newsId.desc())
                .limit(size + 1);

        if (lastNewsId != null && lastNewsId != 0) {
            query.where(news.newsId.lt(lastNewsId));
        }

        return query.fetch();
    }
}
