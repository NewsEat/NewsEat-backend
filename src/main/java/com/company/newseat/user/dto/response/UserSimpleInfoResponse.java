package com.company.newseat.user.dto.response;

public record UserSimpleInfoResponse (
        String nickname
) {
    public static UserSimpleInfoResponse of(String nickname) {
        return new UserSimpleInfoResponse(nickname);
    }
}
