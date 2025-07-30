package com.company.newseat.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    SELF("self");

    private final String provider;
}
