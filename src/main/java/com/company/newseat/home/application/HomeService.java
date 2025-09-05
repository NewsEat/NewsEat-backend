package com.company.newseat.home.application;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.home.dto.response.PositiveNewsListResponse;
import com.company.newseat.home.dto.response.PositiveNewsResponse;
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

}
