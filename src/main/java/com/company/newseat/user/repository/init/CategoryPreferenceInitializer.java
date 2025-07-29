package com.company.newseat.user.repository.init;

import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.global.util.DataInit;
import com.company.newseat.user.domain.CategoryPreference;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.CategoryPreferenceRepository;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DataInit
@Order(3)
public class CategoryPreferenceInitializer implements ApplicationRunner {

    private final CategoryPreferenceRepository categoryPreferenceRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (categoryPreferenceRepository.count() > 0) {
            log.info("[CategoryPreference] 카테고리 선호 더미 데이터 존재");
        } else {
            User guest = userRepository.findById(1L)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            User user = userRepository.findById(2L)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            User admin = userRepository.findById(3L)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            // 카테고리 ID 리스트
            List<Long> categoryIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
            List<Category> categories = categoryRepository.findAllById(categoryIds);

            Category politics = findCategoryById(categories, 1L);
            Category economy = findCategoryById(categories, 2L);
            Category society = findCategoryById(categories, 3L);
            Category lifestyle_culture = findCategoryById(categories, 4L);
            Category tech_science = findCategoryById(categories, 5L);
            Category entertainment = findCategoryById(categories, 6L);
            Category sports = findCategoryById(categories, 7L);
            Category world = findCategoryById(categories, 8L);

            CategoryPreference DUMMY_GUEST_CAT1 = CategoryPreference.of(guest, politics);
            CategoryPreference DUMMY_GUEST_CAT2 = CategoryPreference.of(guest, economy);

            CategoryPreference DUMMY_USER_CAT3 = CategoryPreference.of(user, society);
            CategoryPreference DUMMY_USER_CAT4 = CategoryPreference.of(user, lifestyle_culture);
            CategoryPreference DUMMY_USER_CAT5 = CategoryPreference.of(user, tech_science);

            CategoryPreference DUMMY_ADMIN_CAT6 = CategoryPreference.of(admin, entertainment);
            CategoryPreference DUMMY_ADMIN_CAT7 = CategoryPreference.of(admin, sports);
            CategoryPreference DUMMY_ADMIN_CAT8 = CategoryPreference.of(admin, world);

            List<CategoryPreference> categoryPreferences = new ArrayList<>();
            categoryPreferences.add(DUMMY_GUEST_CAT1);
            categoryPreferences.add(DUMMY_GUEST_CAT2);

            categoryPreferences.add(DUMMY_USER_CAT3);
            categoryPreferences.add(DUMMY_USER_CAT4);
            categoryPreferences.add(DUMMY_USER_CAT5);

            categoryPreferences.add(DUMMY_ADMIN_CAT6);
            categoryPreferences.add(DUMMY_ADMIN_CAT7);
            categoryPreferences.add(DUMMY_ADMIN_CAT8);

            categoryPreferenceRepository.saveAll(categoryPreferences);
        }
    }

    private Category findCategoryById(List<Category> categories, Long id) {
        return categories.stream()
                .filter(c -> c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));
    }
}