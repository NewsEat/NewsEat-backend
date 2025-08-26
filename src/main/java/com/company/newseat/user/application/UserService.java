package com.company.newseat.user.application;

import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.CategoryHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.user.domain.CategoryPreference;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.dto.response.MypageProfileResponse;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    /**
     * 마이페이지에서 프로필 조회 (닉네임, 관심 카테고리)
     */
    public MypageProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<String> categories = user.getPreferences().stream()
                .map(pref -> pref.getCategory().getName())
                .toList();

        return new MypageProfileResponse(user.getNickname(), categories);
    }

    /**
     * 마이페이지에서 닉네임 변경
     */
    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        user.changeNickname(newNickname);
    }

    /**
     * 마이페이지에서 관심 카테고리 수정
     */
    @Transactional
    public void updateCategories(Long userId, List<Long> categoryIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        user.getPreferences().clear();

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new CategoryHandler(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        List<CategoryPreference> newPreferences = categories.stream()
                .map(cat -> CategoryPreference.of(user, cat))
                .toList();

        user.getPreferences().addAll(newPreferences);
    }
}
