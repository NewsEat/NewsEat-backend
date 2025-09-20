package com.company.newseat.home.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.home.dto.response.HomeNewsSectionResponse;
import com.company.newseat.home.dto.response.HomePreferNewsListResponse;
import com.company.newseat.news.dto.response.NewsItemResponse;
import com.company.newseat.home.dto.response.PositiveNewsListResponse;
import com.company.newseat.home.dto.response.HomeNewsListResponse;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    /**
     * 사용자별 긍정 뉴스 5개 조회 (관심 카테고리 3 + 전체 2)
     */
    public PositiveNewsListResponse getHomePositiveNews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<Long> categoryIds = user.getPreferences().stream()
                .map(p -> p.getCategory().getCategoryId())
                .collect(Collectors.toList());

        List<NewsItemResponse> preferredNews = new ArrayList<>();
        if (!categoryIds.isEmpty()) {
            preferredNews = newsRepository.findPreferredPositiveNews(categoryIds, 3);
        }

        List<NewsItemResponse> globalNews = newsRepository.findGlobalPositiveNews(5);

        LinkedHashSet<NewsItemResponse> result = new LinkedHashSet<>();
        result.addAll(preferredNews);
        result.addAll(globalNews);

        List<NewsItemResponse> finalList = result.stream()
                .limit(5)
                .toList();

        return PositiveNewsListResponse.of(finalList);
    }

    /**
     * 사용자 관심 카테고리별 뉴스 5개 조회
     */
    public HomePreferNewsListResponse getHomePrefNewsByCategory(Long userId) {
        User user = userRepository.findByIdWithPreferencesAndCategory(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<HomePreferNewsListResponse.HomeCategoryNews> categoryNewsList = new ArrayList<>();

        for (var preference : user.getPreferences()) {
            Long categoryId = preference.getCategory().getCategoryId();
            String categoryName = preference.getCategory().getName();

            List<NewsItemResponse> newsList;

            if (user.getIsDetox()) {
                newsList = newsRepository.findPreferredPositiveNews(categoryId, 5);
            } else {
                newsList = newsRepository.findByCategoryWithLimit(categoryId, 5);
            }

            categoryNewsList.add(new HomePreferNewsListResponse.HomeCategoryNews(categoryName, newsList));
        }

        return HomePreferNewsListResponse.of(categoryNewsList);
    }

    /**
     * 홈화면 최신 뉴스 조회
     */
    public HomeNewsListResponse getHomeNews(Long userId){
        User user = userRepository.findByIdWithPreferencesAndCategory(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<NewsItemResponse> newsList;

        if (user.getIsDetox()) {
            newsList = newsRepository.findLatestPositiveNews(5);
        } else {
            newsList = newsRepository.findLatestNews(5);
        }

        return HomeNewsListResponse.of(newsList);
    }

    /**
     * 홈화면 뉴스 영역 통합 조회 (관심 카테고리 + 긍정)
     */
    public HomeNewsSectionResponse getHomeNewsSections(Long userId) {
        User user = userRepository.findByIdWithPreferencesAndCategory(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        boolean isDetox = user.getIsDetox();

        Set<Long> shownNewsIds = new HashSet<>();
        List<HomeNewsSectionResponse.Section> sections = new ArrayList<>();

        // 1. 관심 카테고리 뉴스
        List<HomeNewsSectionResponse.Section> prefSections = getHomePrefNewsByCategory(user, shownNewsIds);
        sections.addAll(prefSections);

        // 2. 긍정 뉴스
        List<NewsItemResponse> positiveNews = getHomePositiveNews(user, shownNewsIds);
        sections.add(new HomeNewsSectionResponse.Section("positiveNews", "긍정 뉴스", positiveNews));
        shownNewsIds.addAll(positiveNews.stream().map(NewsItemResponse::newsId).toList());

        return new HomeNewsSectionResponse(isDetox, sections);
    }

    /*
    private List<NewsItemResponse> getHomeLatestNews(User user, Set<Long> shownNewsIds) {
        List<NewsItemResponse> newsList = user.getIsDetox() ?
                newsRepository.findLatestPositiveNews(10) :
                newsRepository.findLatestNews(10);

        newsList = newsList.stream()
                .filter(n -> !shownNewsIds.contains(n.newsId()))
                .limit(5)
                .toList();

        return padWithDummyNews(newsList, 5);
    }
     */

    // 1. 사용자가 선택한 카테고리별 뉴스
    private List<HomeNewsSectionResponse.Section> getHomePrefNewsByCategory(User user, Set<Long> shownNewsIds) {
        List<HomeNewsSectionResponse.Section> sections = new ArrayList<>();

        for (var preference : user.getPreferences()) {
            Long categoryId = preference.getCategory().getCategoryId();
            String categoryName = preference.getCategory().getName();

            List<NewsItemResponse> newsList = user.getIsDetox() ?
                    newsRepository.findPreferredPositiveNews(categoryId, 10) :
                    newsRepository.findByCategoryWithLimit(categoryId, 10);

            newsList = newsList.stream()
                    .filter(n -> !shownNewsIds.contains(n.newsId()))
                    .limit(5)
                    .toList();

            shownNewsIds.addAll(newsList.stream().map(NewsItemResponse::newsId).toList());

            newsList = padWithDummyNews(newsList, 5);

            sections.add(new HomeNewsSectionResponse.Section("prefCategoryNews", categoryName, newsList));
        }
        return sections;
    }

    // 홈에서 긍정 뉴스만 조회하는 메서드 (관심 카테고리 3 + 다른 카테고리 2)
    private List<NewsItemResponse> getHomePositiveNews(User user, Set<Long> shownNewsIds) {
        List<Long> categoryIds = user.getPreferences().stream()
                .map(p -> p.getCategory().getCategoryId())
                .toList();

        List<NewsItemResponse> preferredNews = categoryIds.isEmpty() ?
                Collections.emptyList() :
                newsRepository.findPreferredPositiveNews(categoryIds, 5).stream()
                        .filter(n -> !shownNewsIds.contains(n.newsId()))
                        .toList();

        List<NewsItemResponse> globalNews = newsRepository.findGlobalPositiveNews(5).stream()
                .filter(n -> !shownNewsIds.contains(n.newsId()))
                .toList();

        LinkedHashSet<NewsItemResponse> result = new LinkedHashSet<>();
        result.addAll(preferredNews);
        result.addAll(globalNews);

        List<NewsItemResponse> finalList = result.stream()
                .limit(5)
                .toList();

        return padWithDummyNews(finalList, 5);
    }

    // 뉴스 부족할 때 보여주는 더미 데이터
    private List<NewsItemResponse> padWithDummyNews(List<NewsItemResponse> newsList, int limit) {
        int missing = limit - newsList.size();
        if (missing <= 0) return newsList;

        List<NewsItemResponse> padded = new ArrayList<>(newsList);
        for (int i = 0; i < missing; i++) {
            padded.add(new NewsItemResponse(
                    -1L,              // 더미 ID
                    "https://raw.githubusercontent.com/NewsEat/assets/refs/heads/main/news_eat_logo.png",     // 더미 이미지
                    "곧 업데이트될 뉴스입니다."        // 더미 제목
            ));
        }
        return padded;
    }
}
