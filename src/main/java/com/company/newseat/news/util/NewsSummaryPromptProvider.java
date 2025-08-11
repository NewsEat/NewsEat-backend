package com.company.newseat.news.util;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.NewsHandler;
import org.springframework.stereotype.Component;

@Component
public class NewsSummaryPromptProvider {

    private static final String PROMPT_TEMPLATE = "다음 기사를 핵심 내용 위주로 2~3문장으로 요약해 주세요:\n\n%s";

    public String createPrompt(String newsContent) {
        if (newsContent == null || newsContent.isBlank()) {
            throw new NewsHandler(ErrorStatus.SUMMARY_CONTENT_EMPTY);
        }
        return String.format(PROMPT_TEMPLATE, newsContent);
    }
}
