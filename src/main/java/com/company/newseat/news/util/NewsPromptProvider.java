package com.company.newseat.news.util;

import org.springframework.stereotype.Component;

@Component
public class NewsPromptProvider {

    private static final String SUMMARY_TEMPLATE =
            "다음 기사를 핵심 내용 위주로 2~3문장으로 요약해 주세요:\n\n%s";

    private static final String CONTENT_TEMPLATE =
            "다음 뉴스 제목과 요약을 기반으로 실제 기사처럼 자연스럽게 완결된 뉴스 본문을 700자로 작성해줘. " +
                    "본문만 작성하고 그 외의 다른 문장이나 설명은 하지마\n제목: %s\n요약: %s";

    private static final String IMAGE_KEYWORD_TEMPLATE =
            "다음 뉴스 제목을 보고 이미지 검색에 적합한 간단한 영어 키워드를 딱 1개만 출력해줘. " +
                    "다른 문장이나 설명은 하지마.\n제목: %s";

    public String createSummaryPrompt(String content) {
        return String.format(SUMMARY_TEMPLATE, content);
    }

    public String createContentPrompt(String title, String summary) {
        return String.format(CONTENT_TEMPLATE, title, summary);
    }

    public String createImageKeywordPrompt(String title) {
        return String.format(IMAGE_KEYWORD_TEMPLATE, title);
    }
}
