package com.company.newseat.news.domain.type;

public enum Sentiment {
    POSITIVE("긍정"),
    NEGATIVE("부정"),
    NEUTRAL("중립");

    private final String description;

    Sentiment(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Sentiment fromString(String value) {
        return switch (value.toLowerCase()) {
            case "positive" -> POSITIVE;
            case "negative" -> NEGATIVE;
            case "neutral"  -> NEUTRAL;
            default -> throw new IllegalArgumentException("Unknown Value: " + value);
        };
    }
}
