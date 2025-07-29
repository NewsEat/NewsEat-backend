package com.company.newseat.auth.infrastructure.filter;

import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.info("[인증 실패] 인증 토큰이 없거나 유효하지 않습니다. URI: {}, IP: {}, Exception: {}",
                request.getRequestURI(), request.getRemoteAddr(), authException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String code = ErrorStatus.JWT_INVALID.getCode();
        String message = ErrorStatus.JWT_INVALID.getMessage();

        ApiResponse<ErrorStatus> apiResponse = ApiResponse.onFailure(code, message, null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);

    }
}
