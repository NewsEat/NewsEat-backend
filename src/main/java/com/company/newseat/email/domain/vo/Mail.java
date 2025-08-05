package com.company.newseat.email.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail {
    protected String templateName;
    protected String title;
    protected Map<String, String> values = new HashMap<>();

    protected Mail(String templateName, String title) {
        this.templateName = templateName;
        this.title = title;
    }
}
