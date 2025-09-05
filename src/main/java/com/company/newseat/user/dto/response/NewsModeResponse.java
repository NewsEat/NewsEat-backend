package com.company.newseat.user.dto.response;

public record NewsModeResponse(
        boolean isDetoxMode
) {
    public static NewsModeResponse of (boolean isDetoxMode){
        return new NewsModeResponse(isDetoxMode);
    }
}
