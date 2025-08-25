package com.company.newseat.user.presentation;

import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.user.application.UserService;
import com.company.newseat.user.dto.MypageProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class UserController {

    private final UserService userService;

    @Operation(summary = "마이페이지 프로필 조회", description = "마이페이지에서 닉네임 및 관심사 조회")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MypageProfileResponse>> getProfile(
            @AuthenticationPrincipal Long userId) {

        MypageProfileResponse mypageResponse = userService.getProfile(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(mypageResponse));
    }
}
