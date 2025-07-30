package com.company.newseat.global.validation.validator;

import com.company.newseat.global.validation.annotation.ValidCategoryIdRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CategoryIdRangeValidator implements ConstraintValidator<ValidCategoryIdRange, List<Long>> {

    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return value.stream().allMatch(id -> id >= 1 && id <= 8);
    }
}
