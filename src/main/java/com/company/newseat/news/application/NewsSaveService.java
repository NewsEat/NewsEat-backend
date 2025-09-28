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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
            "정치", List.of("정치", "대통령", "국회", "선거", "정당", "정책"),
            "경제", List.of("경제", "주식", "환율", "금리", "부동산", "재정"),
            "사회", List.of("사회", "범죄", "교육", "환경", "복지", "노동"),
            "생활/문화", List.of("생활", "문화", "여행", "축제", "음식", "건강"),
            "IT/과학", List.of("IT", "과학", "AI", "반도체", "모바일", "우주"),
            "연예", List.of("연예", "가수", "배우", "드라마", "영화", "시상식"),
            "스포츠", List.of("스포츠", "축구", "야구", "올림픽", "농구", "골프"),
            "세계", List.of("세계", "국제", "미국", "중국", "유럽", "북한")
    );

    @Transactional
    public void fetchAndSaveDailyNews(int count) {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {

            List<String> pool = keywordPool.getOrDefault(category.getName(), List.of(category.getName()));
            int dayIndex = LocalDate.now().getDayOfMonth() % pool.size();
            String categoryKeyword = pool.get(dayIndex);

            log.info("카테고리 [{}] → 키워드 [{}] 로 뉴스 검색", category.getName(), categoryKeyword);

            List<NaverNews> newsList = naverNewsApiClient.getNewsList(categoryKeyword, count);

            for (NaverNews n : newsList) {
                if (newsRepository.existsByTitle(n.title())) {
                    continue;
                }
                String content = gptClient.generateNewsContent(n.description());
                String keyword = gptClient.generateImageKeyword(n.title());
                String imageUrl = unsplashApiClient.getImageUrl(keyword);

                if (imageUrl == null || imageUrl.isBlank()) {
                    imageUrl = "https://raw.githubusercontent.com/NewsEat/assets/refs/heads/main/notavailable_image.png"; // 원하는 대체 이미지 URL
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

                String publishedAtStr;
                try {
                    ZonedDateTime zdt = ZonedDateTime.parse(n.pubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
                    publishedAtStr = zdt.toLocalDateTime().format(formatter);
                } catch (Exception e) {
                    publishedAtStr = LocalDateTime.now().toString();
                }

                News news = News.builder()
                        .title(n.title())
                        .content(content)
                        .publisher("Naver")
                        .published_at(publishedAtStr)
                        .imgUrl(imageUrl)
                        .category(category)
                        .build();

                newsRepository.save(news);
            }
        }
    }

}
