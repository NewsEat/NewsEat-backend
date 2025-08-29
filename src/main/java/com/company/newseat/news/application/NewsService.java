package com.company.newseat.news.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.response.*;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.news.util.NewsSummaryPromptProvider;
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
    public SearchNewsResponseList searchNews(String keyword, Long lastNewsId, int size) {

        List<News> newsList = newsRepository.searchByKeywordWithCursor(keyword, lastNewsId, size);

        boolean hasMore = newsList.size() > size;

        List<SearchNewsResponse> list = newsList.stream()
                .limit(size)
                .map(SearchNewsResponse::of)
                .toList();

        return SearchNewsResponseList.of(list, hasMore);
    }

    /**
     * 카테고리별 뉴스 목록 조회
     */
    public CategoryNewsResponseList getCategoryNews(String categoryCode, Long lastNewsId, int size) {

        List<News> newsList = newsRepository.findByCategoryWithCursor(categoryCode, lastNewsId, size);

        boolean hasMore = newsList.size() > size;

        List<CategoryNewsResponse> list = newsList.stream()
                .limit(size)
                .map(CategoryNewsResponse::of)
                .toList();

        return CategoryNewsResponseList.of(list, hasMore);
    }
}
