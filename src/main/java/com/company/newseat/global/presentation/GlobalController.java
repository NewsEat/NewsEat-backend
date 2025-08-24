package com.company.newseat.global.presentation;

import com.company.newseat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GlobalController", description = "서비스 전역 설정 및 상태 확인 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/global")
public class GlobalController {

    @Value("${server.env}")
    private String env;

    @Operation(summary = "health check", description = "배포 및 서비스 상태 확인을 위한 API")
    @GetMapping("/health-check")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.onSuccess(env));
    }
}
