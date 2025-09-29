package com.company.newseat.news.application;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.bookmark.repository.BookmarkRepository;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.news.dto.response.*;
import com.company.newseat.news.infrastructure.GptClient;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final GptClient gptClient;
    private final NewsRepository newsRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    /**
     * open ai 이용한 뉴스 요약 by newsId
     */
    public NewsSummaryResponse summarizeNewsById(Long newsId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        String title = news.getTitle();
        String sentiment = Optional.ofNullable(news.getSentiment())
                .map(Sentiment::getDescription)
                .orElse("감정 정보 없음");
        String summaryText = gptClient.generateSummary(news.getContent());

        return NewsSummaryResponse.of(title, sentiment, summaryText);
    }

    /**
     * 키워드로 뉴스 검색
     */
    public SearchNewsListResponse searchNews(String keyword, Long lastNewsId, int size) {

        List<News> newsList = newsRepository.searchByKeywordWithCursor(keyword, lastNewsId, size);

        boolean hasMore = newsList.size() > size;

        List<SearchNewsResponse> list = newsList.stream()
                .limit(size)
                .map(SearchNewsResponse::from)
                .toList();

        return SearchNewsListResponse.of(list, hasMore);
    }

    /**
     * 카테고리별 뉴스 목록 조회
     */
    public CategoryNewsListResponse getCategoryNews(String categoryCode, Long lastNewsId, int size) {

        List<News> newsList = newsRepository.findByCategoryWithCursor(categoryCode, lastNewsId, size);

        boolean hasMore = newsList.size() > size;

        List<CategoryNewsResponse> list = newsList.stream()
                .limit(size)
                .map(CategoryNewsResponse::from)
                .toList();

        return CategoryNewsListResponse.of(list, hasMore);
    }

    /**
     * 뉴스 단건 조회
     */
    public NewsDetailResponse getNewsDetail(Long userId, Long newsId){
        News news = newsRepository.findByIdWithCategory(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Bookmark> bookmarkOpt = bookmarkRepository.findByUserAndNewsId(user, newsId);
        Long bookmarkId = bookmarkOpt.map(Bookmark::getBookmarkId).orElse(null);

        return NewsDetailResponse.from(news, bookmarkId);
    }

    /**
     * 뉴스 하단 제안 뉴스 조회
     */
    public SuggestedNewsListResponse getSuggestedNews(Long newsId){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        Long categoryId = news.getCategory().getCategoryId();

        List<NewsItemResponse> newsList = newsRepository.findByCategoryWithLimitExcludingNews(categoryId, 5, newsId);

        return SuggestedNewsListResponse.of(newsList);
    }
}
