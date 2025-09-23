package com.company.newseat.user.presentation;

import com.company.newseat.auth.application.AuthService;
import com.company.newseat.auth.dto.request.ResetPasswordRequest;
import com.company.newseat.global.response.ApiResponse;
import com.company.newseat.user.dto.request.NewsModeRequest;
import com.company.newseat.user.dto.response.NewsModeResponse;
import com.company.newseat.user.application.UserService;
import com.company.newseat.user.dto.request.UpdateCategoryRequest;
import com.company.newseat.user.dto.request.UpdateNicknameRequest;
import com.company.newseat.user.dto.response.MypageProfileResponse;
import com.company.newseat.user.dto.response.UserSimpleInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final AuthService authService;

    @Operation(summary = "마이페이지 프로필 조회", description = "마이페이지에서 닉네임 및 관심사 조회")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MypageProfileResponse>> getProfile(
            @AuthenticationPrincipal Long userId) {

        MypageProfileResponse mypageResponse = userService.getProfile(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(mypageResponse));
    }

    @Operation(summary = "마이페이지 닉네임 수정", description = "닉네임은 필수 입력이며 한국어/영어/숫자로 최대 10자 입력 가능")
    @PatchMapping("/profile/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UpdateNicknameRequest request) {

        userService.updateNickname(userId, request.nickname());

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "마이페이지 관심 카테고리 수정",
            description = "관심 카테고리는 최소 1개, 최대 3개까지 선택 가능 <br>" +
                    "카테고리: 정치(1), 경제(2), 사회(3), 생활/문화(4), IT/과학(5), 연예(6), 스포츠(7), 세계(8)")
    @PutMapping("/categories")
    public ResponseEntity<ApiResponse<Void>> updateCategories(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UpdateCategoryRequest request) {

        userService.updateCategories(userId, request.categoryIds());

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "뉴스 디톡스 모드 설정", description = "뉴스 디톡스 모드 설정")
    @PutMapping("/detox-mode")
    public ResponseEntity<ApiResponse<NewsModeResponse>> updateDetoxMode(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NewsModeRequest request){

        NewsModeResponse response = userService.updateDetoxMode(userId, request.isDetoxMode());

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "내 정보 조회 (전역에서 사용)", description = "현재 로그인한 유저의 기본정보(닉네임) 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSimpleInfoResponse>> getMyInfo(
            @AuthenticationPrincipal Long userId){

        UserSimpleInfoResponse response = userService.getMyInfo(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "마이페이지 비밀번호 변경", description = "현재 로그인한 사용자의 비밀번호 변경")
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ResetPasswordRequest request) {

        authService.changePassword(userId, request);

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
