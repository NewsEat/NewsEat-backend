package com.company.newseat.news.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.request.NewsSummaryRequest;
import com.company.newseat.news.dto.response.NewsSummaryResponse;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.news.util.NewsSummaryPromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.Collections;

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
        Prompt prompt = new Prompt(Collections.singletonList(new UserMessage(promptText)));
        ChatResponse response;
        try {
            response = openAiChatModel.call(prompt);
        } catch (Exception e) {
            throw new NewsHandler(ErrorStatus.SUMMARY_GENERATION_FAILED);
        }
        Generation generation = response.getResult();

        return generation.getOutput().getText();
    }
}
