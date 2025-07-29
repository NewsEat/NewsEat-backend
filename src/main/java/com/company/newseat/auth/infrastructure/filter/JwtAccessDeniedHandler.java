package com.company.newseat.auth.infrastructure.filter;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        log.warn("[인가 실패] 권한 부족으로 접근 거부 - URI: {}, IP: {}, Exception: {}",
                request.getRequestURI(), request.getRemoteAddr(), accessDeniedException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String code = ErrorStatus.ACCESS_DENIED.getCode();
        String message = ErrorStatus.ACCESS_DENIED.getMessage();

        ApiResponse<ErrorStatus> apiResponse = ApiResponse.onFailure(code, message, null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(jsonResponse);
    }
}
