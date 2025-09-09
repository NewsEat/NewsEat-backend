package com.company.newseat.news.repository;

import com.company.newseat.home.dto.response.HomePreferNewsResponse;
import com.company.newseat.home.dto.response.PositiveNewsResponse;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.company.newseat.category.domain.QCategory.category;
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


    @Override
    public List<News> findByCategoryWithCursor(String categoryCode, Long lastNewsId, int size) {
        var query = queryFactory
                .selectFrom(news)
                .join(news.category, category).fetchJoin()
                .where(category.code.eq(categoryCode))
                .orderBy(news.newsId.desc())
                .limit(size + 1);

        if (lastNewsId != null && lastNewsId != 0) {
            query.where(news.newsId.lt(lastNewsId));
        }

        return query.fetch();
    }

    @Override
    public List<PositiveNewsResponse> findGlobalPositiveNews(int limit){
        var query = queryFactory
                .select(Projections.constructor(
                        PositiveNewsResponse.class,
                        news.imgUrl,
                        news.title
                ))
                .from(news)
                .where(news.sentiment.eq(Sentiment.POSITIVE))
                .orderBy(news.published_at.desc())
                .limit(limit);

        return query.fetch();
    }

    @Override
    public List<PositiveNewsResponse> findPreferredPositiveNews(List<Long> categoryIds, int limit) {
        var query = queryFactory
                .select(Projections.constructor(
                        PositiveNewsResponse.class,
                        news.imgUrl,
                        news.title
                ))
                .from(news)
                .where(
                        news.sentiment.eq(Sentiment.POSITIVE)
                                .and(news.category.categoryId.in(categoryIds))
                )
                .orderBy(news.published_at.desc())
                .limit(limit);

        return query.fetch();
    }

    @Override
    public List<HomePreferNewsResponse> findPreferredPositiveNews(Long categoryId, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        HomePreferNewsResponse.class,
                        news.imgUrl,
                        news.title
                ))
                .from(news)
                .where(
                        news.sentiment.eq(Sentiment.POSITIVE)
                                .and(news.category.categoryId.eq(categoryId))
                )
                .orderBy(news.published_at.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<HomePreferNewsResponse> findByCategoryWithLimit(Long categoryId, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        HomePreferNewsResponse.class,
                        news.imgUrl,
                        news.title
                ))
                .from(news)
                .where(news.category.categoryId.eq(categoryId))
                .orderBy(news.published_at.desc())
                .limit(limit)
                .fetch();
    }
}
