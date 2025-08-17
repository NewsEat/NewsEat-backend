package com.company.newseat.news.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.request.NewsSummaryRequest;
import com.company.newseat.news.dto.response.NewsSummaryResponse;
import com.company.newseat.news.dto.response.SearchNewsResponse;
import com.company.newseat.news.dto.response.SearchNewsResponseList;
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

        String newsContent = news.getContent();
        String summaryText = generateSummaryFromContent(newsContent);

        return new NewsSummaryResponse(summaryText);
    }

    /**
     * open ai 이용한 뉴스 요약 by content
     */
    public NewsSummaryResponse summarizeNewsByContent(NewsSummaryRequest request) {

        String newsContent = request.newsContent();
        String summaryText = generateSummaryFromContent(newsContent);

        return new NewsSummaryResponse(summaryText);
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
}
