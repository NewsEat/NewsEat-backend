package com.company.newseat.email.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BooleanType {

    T(true),
    F(false);

    private final boolean identifier;

    public static BooleanType of(boolean identifier) {
        return identifier ? T : F;
    }
}
