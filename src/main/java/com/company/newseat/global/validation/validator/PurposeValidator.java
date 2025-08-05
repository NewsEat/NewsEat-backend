package com.company.newseat.global.validation.validator;

import com.company.newseat.email.domain.type.Purpose;
import com.company.newseat.global.validation.annotation.ValidPurpose;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PurposeValidator implements ConstraintValidator<ValidPurpose, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return !Purpose.of(value).equals(Purpose.NONE);
    }
}
