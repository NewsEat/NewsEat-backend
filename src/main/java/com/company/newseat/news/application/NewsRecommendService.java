package com.company.newseat.news.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.dto.response.NewsItemResponse;
import com.company.newseat.news.dto.response.SuggestedNewsListResponse;
import com.company.newseat.news.infrastructure.RecommendApiClient;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.newslog.repository.NewsLogRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsRecommendService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final NewsLogRepository newsLogRepository;
    private final RecommendApiClient recommendApiClient;

//    public SuggestedNewsListResponse getSuggestedNewsFromFlask(Long userId, Long newsId) {
//        List<Long> recommendedIds = recommendApiClient.getRecommendedIds(newsId);
//
//        List<News> newsList = newsRepository.findAllById(recommendedIds);
//
//        if (newsList.size() < 5) {
//            int remaining = 5 - newsList.size();
//
//            List<News> additionalNews = newsRepository.findRandomNewsExcludingIds(
//                    recommendedIds, remaining
//            );
//            newsList.addAll(additionalNews);
//        }
//
//        List<NewsItemResponse> dtoList = newsList.stream()
//                .map(NewsItemResponse::from)
//                .toList();
//
//        return SuggestedNewsListResponse.of(dtoList);
//    }

    private static final int TARGET_COUNT = 5;

    public SuggestedNewsListResponse getSuggestedNewsFromFlask(Long newsId) {
        List<Long> recommendedIds = recommendApiClient.getRecommendedIds(newsId);

        if (recommendedIds.size() > TARGET_COUNT) {
            recommendedIds = recommendedIds.subList(0, TARGET_COUNT);
        }

        List<News> newsList = newsRepository.findAllById(recommendedIds);

        if (newsList.size() < TARGET_COUNT) {
            int remaining = TARGET_COUNT - newsList.size();
            List<News> additionalNews = newsRepository.findRandomNewsExcludingIds(
                    recommendedIds.isEmpty() ? List.of(-1L) : recommendedIds,
                    remaining
            );
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
    public SuggestedNewsListResponse getHomeRecommendedNews(Long userId) {

        User user = userRepository.findByIdWithPreferencesAndCategory(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean isDetox = user.getIsDetox();

        List<Long> recentNewsIds = newsLogRepository.findRecentNewsIdsByUserId(userId, 5);

        if (recentNewsIds.isEmpty()) {
            return fallbackToLatestNews(isDetox);
        }

        List<Long> recommendedIds = recommendApiClient.getRecommendedIdsForUser(recentNewsIds);

        if (recommendedIds.size() > 10) {
            recommendedIds = recommendedIds.subList(0, 10);
        }

        List<News> newsList = newsRepository.findAllById(recommendedIds);

        if (isDetox) {
            newsList = newsList.stream()
                    .filter(news -> news.getSentiment() != null && news.getSentiment().equals("positive"))
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

        return SuggestedNewsListResponse.of(finalList);
    }

    private SuggestedNewsListResponse fallbackToLatestNews(boolean isDetox) {
        List<NewsItemResponse> newsList;

        if (isDetox) {
            newsList = newsRepository.findLatestPositiveNews(TARGET_COUNT);
        } else {
            newsList = newsRepository.findLatestNews(TARGET_COUNT);
        }

        return SuggestedNewsListResponse.of(
                padWithDummyNews(newsList, TARGET_COUNT)
        );
    }
}
