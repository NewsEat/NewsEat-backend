package com.company.newseat.category.repository.init;

import java.util.ArrayList;
import java.util.List;

import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.global.util.DataInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

@Slf4j
@RequiredArgsConstructor
@Order(1)
@DataInit
public class CategoryInitializer implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (categoryRepository.count() > 0) {
            log.info("[Category] 카테고리 기본 데이터 존재");
        } else {
            List<Category> categoryList = new ArrayList<>();

            Category CATEGORY_POLITICS = Category.builder()
                    .name("정치")
                    .code("001")
                    .orderIndex(1)
                    .build();
            Category CATEGORY_ECONOMY = Category.builder()
                    .name("경제")
                    .code("002")
                    .orderIndex(2)
                    .build();
            Category CATEGORY_SOCIETY = Category.builder()
                    .name("사회")
                    .code("003")
                    .orderIndex(3)
                    .build();
            Category CATEGORY_LIFESTYLE_CULTURE = Category.builder()
                    .name("생활/문화")
                    .code("004")
                    .orderIndex(4)
                    .build();
            Category CATEGORY_TECH_SCIENCE = Category.builder()
                    .name("IT/과학")
                    .code("005")
                    .orderIndex(5)
                    .build();
            Category CATEGORY_ENTERTAINMENT = Category.builder()
                    .name("연예")
                    .code("006")
                    .orderIndex(6)
                    .build();
            Category CATEGORY_SPORTS = Category.builder()
                    .name("스포츠")
                    .code("007")
                    .orderIndex(7)
                    .build();
            Category CATEGORY_WORLD = Category.builder()
                    .name("세계")
                    .code("008")
                    .orderIndex(8)
                    .build();

            categoryList.add(CATEGORY_POLITICS);
            categoryList.add(CATEGORY_ECONOMY);
            categoryList.add(CATEGORY_SOCIETY);
            categoryList.add(CATEGORY_LIFESTYLE_CULTURE);
            categoryList.add(CATEGORY_TECH_SCIENCE);
            categoryList.add(CATEGORY_ENTERTAINMENT);
            categoryList.add(CATEGORY_SPORTS);
            categoryList.add(CATEGORY_WORLD);

            categoryRepository.saveAll(categoryList);
        }
    }
}
