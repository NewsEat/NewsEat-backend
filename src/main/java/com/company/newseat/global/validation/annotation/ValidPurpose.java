package com.company.newseat.global.validation.annotation;

import com.company.newseat.global.validation.validator.PurposeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Constraint(validatedBy = PurposeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidPurpose {
    String message() default "유효하지 않은 이메일 인증 목적입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
