package com.company.newseat.auth.type;

import lombok.Getter;

@Getter
public enum JwtValidationResult {
    VALID_JWT("Valid token"),
    INVALID_JWT_SIGNATURE("Invalid JWT Signature"),
    INVALID_JWT("Invalid JWT"),
    EXPIRED_JWT("Expired JWT"),
    UNSUPPORTED_JWT("Unsupported JWT"),
    EMPTY_JWT("Empty JWT");

    private final String message;

    JwtValidationResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
