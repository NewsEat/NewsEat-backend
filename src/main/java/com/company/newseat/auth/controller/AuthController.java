package com.company.newseat.auth.controller;

import com.company.newseat.auth.application.AuthService;
import com.company.newseat.auth.dto.request.VerifyEmailCodeRequest ;
import com.company.newseat.auth.dto.request.LoginRequest;
import com.company.newseat.auth.dto.request.SendEmailCodeRequest;
import com.company.newseat.auth.dto.request.SignUpRequest;
import com.company.newseat.auth.dto.response.*;
import com.company.newseat.email.domain.EmailAuth;
import com.company.newseat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원가입, 로그인, 토큰 재발급 등 인증 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @RequestBody @Valid SignUpRequest request) {

        SignUpResponse signUpResponse = authService.signUp(request);

        return ResponseEntity.ok(ApiResponse.onSuccess(signUpResponse));
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody @Valid LoginRequest request, HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request, response);

        return ResponseEntity.ok(ApiResponse.onSuccess(loginResponse));
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal Long userId) {

        authService.withdraw(userId);

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "access Token 재발급", description = "access token 재발급")
    @PostMapping("/tokens/reissue")
    public ResponseEntity<ApiResponse<ReissueResponse>> reissueToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String refreshToken) {

        ReissueResponse response = authService.reissueToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "테스트용 토큰발급", description = "테스트용 토큰발급")
    @GetMapping("/tokens/test/{userId}")
    public String testToken(@PathVariable Long userId) {
        return authService.getTestToken(userId);
    }

    @Operation(summary = "인증 이메일 전송", description = "인증 이메일 전송")
    @PostMapping("/email")
    public ResponseEntity<ApiResponse<SendEmailCodeResponse>> sendCodeMail(
            @RequestBody @Valid SendEmailCodeRequest request) {

        EmailAuth emailAuth = authService.sendCodeMail(request);
        SendEmailCodeResponse response = SendEmailCodeResponse.of(emailAuth);

        return  ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "이메일 인증 확인", description = "이메일 인증 확인")
    @PostMapping("/email/check")
    public ResponseEntity<ApiResponse<VerifyEmailCodeResponse>> checkCodeMail(
            @RequestBody @Valid VerifyEmailCodeRequest request) {

        boolean isChecked = authService.verifyEmailCode(request);
        VerifyEmailCodeResponse response = VerifyEmailCodeResponse.of(isChecked);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
