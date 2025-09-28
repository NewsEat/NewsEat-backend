package com.company.newseat.news.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.openai.OpenAiChatModel;

@Component
@RequiredArgsConstructor
public class GptClient {

    private final OpenAiChatModel openAiChatModel;

    /**
     * 뉴스 본문 생성 (summary를 바탕으로 가상의 뉴스 본문 생성)
     */
    public String generateNewsContent(String summary) {
        String prompt = "다음 뉴스 요약을 기반으로 현실적인 뉴스 본문을 700자로 작성해줘. 본문만 작성하고 그 외의 다른 문장이나 설명은 하지마:\n" + summary;
        try {
            return openAiChatModel.call(prompt);
        } catch (Exception e) {
            throw new RuntimeException("뉴스 본문 생성 실패", e);
        }
    }

    /**
     * 뉴스 이미지 url 검색을 위한 검색 키워드 생성
     */
    public String generateImageKeyword(String title) {
        String prompt = "다음 뉴스 제목을 보고 이미지 검색에 적합한 간단한 영어 키워드를 딱 1개만 출력해줘. 다른 문장이나 설명은 하지마.\n" +
                "제목: " + title;
        try {
            return openAiChatModel.call(prompt)
                    .replaceAll("[^a-zA-Z0-9 ]", "") // 특수문자 제거
                    .trim();
        } catch (Exception e) {
            throw new RuntimeException("이미지 키워드 생성 실패", e);
        }
    }
}