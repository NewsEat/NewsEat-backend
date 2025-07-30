package com.company.newseat.global.validation.annotation;

import com.company.newseat.global.validation.validator.CategoryIdRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CategoryIdRangeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidCategoryIdRange {
    String message() default "카테고리 ID는 1부터 8 사이 값만 허용됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
