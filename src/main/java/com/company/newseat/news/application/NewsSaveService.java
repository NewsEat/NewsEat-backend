package com.company.newseat.news.application;

import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.response.NaverNews;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.news.infrastructure.GptClient;
import com.company.newseat.news.infrastructure.NaverNewsApiClient;
import com.company.newseat.news.infrastructure.UnsplashApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSaveService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    private final NaverNewsApiClient naverNewsApiClient;
    private final UnsplashApiClient unsplashApiClient;
    private final GptClient gptClient;

    private static final Map<String, List<String>> keywordPool = Map.of(
            "정치", List.of("정치", "대통령", "국회", "선거", "정당", "정책", "외교", "법률"),
            "경제", List.of("경제", "주식", "환율", "금리", "부동산", "재정", "금융", "기업"),
            "사회", List.of("사회", "범죄", "교육", "환경", "복지", "노동", "교통", "보건"),
            "생활/문화", List.of("생활", "문화", "여행", "축제", "음식", "건강", "패션", "음악"),
            "IT/과학", List.of("IT", "과학", "AI", "반도체", "모바일", "우주", "로봇", "인터넷"),
            "연예", List.of("연예", "가수", "배우", "드라마", "영화", "시상식", "예능", "뮤지컬"),
            "스포츠", List.of("스포츠", "축구", "야구", "올림픽", "농구", "골프", "테니스", "배드민턴"),
            "세계", List.of("세계", "국제", "분쟁", "기후", "유럽", "아시아", "아프리카", "오세아니아")
    );
    private static final String DEFAULT_IMAGE_URL = "https://raw.githubusercontent.com/NewsEat/assets/refs/heads/main/notavailable_image.png";

    public void fetchAndSaveDailyNews(int count) {
        log.info("=== 뉴스 수집 시작 ===");

        List<Category> categories = categoryRepository.findAll();
        int totalSuccess = 0;
        int totalFail = 0;

        for (Category category : categories) {
            try {
                int saved = processCategoryNews(category, count);
                totalSuccess += saved;
                log.info("카테고리 [{}] 처리 완료: {}건 저장", category.getName(), saved);
            } catch (Exception e) {
                totalFail += count;
                log.error("카테고리 [{}] 처리 실패: {}", category.getName(), e.getMessage());
            }
        }

        log.info("=== 뉴스 수집 완료 - 성공: {}건, 실패: {}건 ===", totalSuccess, totalFail);
    }

    @Transactional
    public int processCategoryNews(Category category, int count) {
        List<String> pool = keywordPool.getOrDefault(category.getName(), List.of(category.getName()));
        List<String> copyPool = new ArrayList<>(pool);

        int savedCount = 0;
        for (int i = 0; i < count; i++) {
            if (copyPool.isEmpty()) break;
            
            int idx = (int) (Math.random() * copyPool.size());
            String keyword = copyPool.remove(idx);

            int recentLimit = 50;
            int maxStart = Math.max(1, recentLimit - 1);
            int start = 1 + (int)(Math.random() * maxStart);

            log.info("카테고리 [{}] → 키워드 [{}], start [{}]", category.getName(), keyword, start);

            List<NaverNews> newsList;
            try {
                newsList = naverNewsApiClient.getNewsList(keyword, 1, start);
            } catch (Exception e) {
                log.error("네이버 API 호출 실패: {}", e.getMessage());
                continue;
            }

            for (NaverNews naverNews : newsList) {
                try {
                    if (saveNews(naverNews, category)) {
                        savedCount++;
                    }
                } catch (Exception e) {
                    log.error("뉴스 저장 실패 [{}]: {}", naverNews.title(), e.getMessage());
                }
            }
        }
        return savedCount;
    }

    /**
     * 뉴스 저장
     */
    private boolean saveNews(NaverNews naverNews, Category category) {
        if (newsRepository.existsByTitle(naverNews.title())) {
            return false;
        }
        
        String content;
        try {
            content = gptClient.generateNewsContent(naverNews.title(), naverNews.description());
        } catch (Exception e) {
            log.warn("GPT 본문 생성 실패 - 요약 사용: {}", e.getMessage());
            content = naverNews.description();
        }

        String imageUrl = getImageUrl(naverNews.title());
        String publishedAt = parsePublishDate(naverNews.pubDate());

        News news = News.builder()
                .title(naverNews.title())
                .content(content)
                .publisher("Naver")
                .published_at(publishedAt)
                .imgUrl(imageUrl)
                .category(category)
                .build();

        newsRepository.save(news);
        return true;
    }

    private String getImageUrl(String title) {
        try {
            String keyword = gptClient.generateImageKeyword(title);
            String url = unsplashApiClient.getImageUrl(keyword);

            if (url != null && !url.isBlank()) {
                return url;
            }
        } catch (Exception e) {
            log.warn("이미지 URL 가져오기 실패: {}", e.getMessage());
        }

        return DEFAULT_IMAGE_URL;
    }

    private String parsePublishDate(String pubDate) {
        try {
            ZonedDateTime zdt = ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
            return zdt.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        } catch (Exception e) {
            log.warn("날짜 파싱 실패 - 현재 시각 사용: {}", pubDate);
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        }
    }
}
