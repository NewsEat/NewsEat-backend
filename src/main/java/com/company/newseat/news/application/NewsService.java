package com.company.newseat.news.application;

import com.company.newseat.bookmark.repository.BookmarkRepository;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.response.*;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.news.util.NewsSummaryPromptProvider;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final OpenAiChatModel openAiChatModel;
    private final NewsRepository newsRepository;
    private final NewsSummaryPromptProvider promptProvider;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    /**
     * open ai 이용한 뉴스 요약 by newsId
     */
    public NewsSummaryResponse summarizeNewsById(Long newsId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        String title = news.getTitle();
        String sentiment = news.getSentiment().getDescription();

        String newsContent = news.getContent();
        String summaryText = generateSummaryFromContent(newsContent);

        return new NewsSummaryResponse(title, sentiment, summaryText);
    }

    private String generateSummaryFromContent(String content) {
        String promptText = promptProvider.createPrompt(content);
        try {
            return openAiChatModel.call(promptText);
        } catch (Exception e) {
            throw new NewsHandler(ErrorStatus.SUMMARY_GENERATION_FAILED);
        }
    }

    /**
     * 키워드로 뉴스 검색
     */
    public SearchNewsListResponse searchNews(String keyword, Long lastNewsId, int size) {

        List<News> newsList = newsRepository.searchByKeywordWithCursor(keyword, lastNewsId, size);

        boolean hasMore = newsList.size() > size;

        List<SearchNewsResponse> list = newsList.stream()
                .limit(size)
                .map(SearchNewsResponse::of)
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
                .map(CategoryNewsResponse::of)
                .toList();

        return CategoryNewsListResponse.of(list, hasMore);
    }

    /**
     * 뉴스 단건 조회
     */
    public NewsDetailResponse getNewsDetail(Long userId, Long newsId){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean isBookmarked = bookmarkRepository.existsByUserAndNewsId(user, newsId);

        return NewsDetailResponse.of(news, isBookmarked);
    }
}
