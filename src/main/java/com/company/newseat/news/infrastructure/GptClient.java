package com.company.newseat.news.infrastructure;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.news.util.NewsPromptProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.openai.OpenAiChatModel;

@Component
@RequiredArgsConstructor
public class GptClient {

    private final OpenAiChatModel openAiChatModel;
    private final NewsPromptProvider promptProvider;

    /**
     * 뉴스 요약 생성
     */
    public String generateSummary(String content) {
        String prompt = promptProvider.createSummaryPrompt(content);
        return callModel(prompt, ErrorStatus.SUMMARY_GENERATION_FAILED);
    }

    /**
     * 뉴스 본문 생성 (summary를 바탕으로 가상의 뉴스 본문 생성)
     */
    public String generateNewsContent(String summary) {
        String prompt = promptProvider.createContentPrompt(summary);
        return callModel(prompt, ErrorStatus.CONTENT_GENERATION_FAILED);
    }

    /**
     * 뉴스 이미지 url 검색을 위한 검색 키워드 생성
     */
    public String generateImageKeyword(String title) {
        String prompt = promptProvider.createImageKeywordPrompt(title);
        return callModel(prompt, ErrorStatus.IMAGE_KEYWORD_GENERATION_FAILED)
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .trim();
    }

    private String callModel(String prompt, ErrorStatus errorStatus) {
        try {
            return openAiChatModel.call(prompt);
        } catch (Exception e) {
            throw new NewsHandler(errorStatus);
        }
    }
}