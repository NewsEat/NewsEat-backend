package com.company.newseat.home.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.home.dto.response.HomePreferNewsListResponse;
import com.company.newseat.home.dto.response.HomePreferNewsResponse;
import com.company.newseat.home.dto.response.PositiveNewsListResponse;
import com.company.newseat.home.dto.response.PositiveNewsResponse;
import com.company.newseat.home.dto.response.HomeNewsListResponse;
import com.company.newseat.home.dto.response.HomeNewsResponse;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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

        List<PositiveNewsResponse> preferredNews = new ArrayList<>();
        if (!categoryIds.isEmpty()) {
            preferredNews = newsRepository.findPreferredPositiveNews(categoryIds, 3);
        }

        List<PositiveNewsResponse> globalNews = newsRepository.findGlobalPositiveNews(5);

        LinkedHashSet<PositiveNewsResponse> result = new LinkedHashSet<>();
        result.addAll(preferredNews);
        result.addAll(globalNews);

        List<PositiveNewsResponse> finalList = result.stream()
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

            List<HomePreferNewsResponse> newsList;

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

        List<HomeNewsResponse> newsList;

        if (user.getIsDetox()) {
            newsList = newsRepository.findLatestPositiveNews(5);
        } else {
            newsList = newsRepository.findLatestNews(5);
        }

        return HomeNewsListResponse.of(newsList);
    }
}
