package com.company.newseat.news.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.home.dto.response.HomeNewsListResponse;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.news.dto.response.NewsItemResponse;
import com.company.newseat.news.dto.response.SuggestedNewsListResponse;
import com.company.newseat.news.infrastructure.RecommendApiClient;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.newslog.repository.NewsLogRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsRecommendService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final NewsLogRepository newsLogRepository;
    private final RecommendApiClient recommendApiClient;

    private static final int TARGET_COUNT = 5;

    public SuggestedNewsListResponse getSuggestedNewsFromFlask(Long newsId) {
        List<Long> recommendedIds;
        try {
            recommendedIds = recommendApiClient.getRecommendedIds(newsId);
        } catch (Exception e) {
            log.warn("Flask 추천 API 호출 실패: {}, fallback to 랜덤 뉴스", e.getMessage());
            recommendedIds = List.of();
        }

        if (recommendedIds.size() > TARGET_COUNT) {
            recommendedIds = recommendedIds.subList(0, TARGET_COUNT);
        }

        List<News> newsList = new ArrayList<>(newsRepository.findAllById(recommendedIds));

        if (newsList.size() < TARGET_COUNT) {
            int remaining = TARGET_COUNT - newsList.size();
            List<Long> excludedIds = recommendedIds.isEmpty() ? List.of(-1L) : recommendedIds;
            List<News> additionalNews = new ArrayList<>(newsRepository.findRandomNewsExcludingIds(excludedIds, remaining));
            newsList.addAll(additionalNews);
        }

        List<NewsItemResponse> dtoList = newsList.stream()
                .map(NewsItemResponse::from)
                .toList();

        List<NewsItemResponse> finalList = padWithDummyNews(dtoList, TARGET_COUNT);

        return SuggestedNewsListResponse.of(finalList);
    }

    private List<NewsItemResponse> padWithDummyNews(List<NewsItemResponse> newsList, int limit) {
        int missing = limit - newsList.size();
        if (missing <= 0) return newsList;

        List<NewsItemResponse> padded = new ArrayList<>(newsList);
        for (int i = 0; i < missing; i++) {
            padded.add(new NewsItemResponse(
                    -1L,
                    "https://raw.githubusercontent.com/NewsEat/assets/refs/heads/main/dummy_image.png",
                    "곧 업데이트될 뉴스입니다."
            ));
        }
        return padded;
    }

    /**
     * 홈화면 추천 뉴스
     */
    public HomeNewsListResponse getHomeRecommendedNews(Long userId) {

        User user = userRepository.findByIdWithPreferencesAndCategory(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean isDetox = user.getIsDetox();

        List<Long> recentNewsIds = newsLogRepository.findRecentNewsIdsByUserId(userId, 5);
        if (recentNewsIds.isEmpty()) {
            return fallbackToLatestNews(isDetox);
        }

        List<Long> recommendedIds;
        try {
            recommendedIds = recommendApiClient.getRecommendedIdsForUser(recentNewsIds);
        } catch (Exception e) {
            log.warn("Flask 추천 API 호출 실패: {}, fallback to latest news", e.getMessage());
            return fallbackToLatestNews(isDetox);
        }

        if (recommendedIds.size() > 10) {
            recommendedIds = recommendedIds.subList(0, 10);
        }

        Map<Long, News> newsMap = newsRepository.findAllById(recommendedIds).stream()
                .collect(Collectors.toMap(News::getNewsId, n -> n));

        List<News> newsList = recommendedIds.stream()
                .map(newsMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (isDetox) {
            newsList = newsList.stream()
                    .filter(news -> news.getSentiment() != null && news.getSentiment().equals(Sentiment.POSITIVE))
                    .toList();
        }

        if (newsList.size() < TARGET_COUNT) {
            int remaining = TARGET_COUNT - newsList.size();
            List<Long> excludedIds = recommendedIds.isEmpty() ? List.of(-1L) : recommendedIds;
            List<News> additional = newsRepository.findRandomNewsExcludingIds(excludedIds, remaining);
            newsList = new ArrayList<>(newsList);
            newsList.addAll(additional);
        }

        List<NewsItemResponse> dtoList = newsList.stream()
                .map(NewsItemResponse::from)
                .toList();

        List<NewsItemResponse> finalList = padWithDummyNews(dtoList, TARGET_COUNT);

        return HomeNewsListResponse.of(finalList);
    }

    private HomeNewsListResponse fallbackToLatestNews(boolean isDetox) {
        List<NewsItemResponse> newsList;

        if (isDetox) {
            newsList = newsRepository.findLatestPositiveNews(TARGET_COUNT);
        } else {
            newsList = newsRepository.findLatestNews(TARGET_COUNT);
        }

        return HomeNewsListResponse.of(
                padWithDummyNews(newsList, TARGET_COUNT)
        );
    }
}
