package com.company.newseat.user.dto.request;

import com.company.newseat.global.validation.annotation.ValidCategoryIdRange;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCategoryRequest(
        @ValidCategoryIdRange
        @Size(min = 1, max = 3, message = "관심 카테고리는 최소 1개, 최대 3개까지 선택 가능합니다.")
        List<Long> categoryIds
) {
}
