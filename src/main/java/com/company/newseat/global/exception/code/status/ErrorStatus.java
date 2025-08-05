package com.company.newseat.global.exception.code.status;

import com.company.newseat.global.exception.code.BaseErrorCode;
import com.company.newseat.global.exception.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Auth
    JWT_EXPIRED(UNAUTHORIZED, "AUTH4011", "토큰이 만료되었습니다."),
    JWT_INVALID(UNAUTHORIZED, "AUTH4012", "유효하지 않거나 거부된 토큰입니다."),
    LOGIN_FAILED_INCORRECT_PASSWORD(UNAUTHORIZED,"AUTH4013", "비밀번호가 일치하지 않습니다."),

    EMAIL_INVALID_FORMAT(BAD_REQUEST, "AUTH4001", "이메일 형식이 유효하지 않습니다."),
    EMAIL_ALREADY_EXISTS(BAD_REQUEST, "AUTH4002", "이미 가입된 이메일입니다."),
    EMAIL_NOT_VERIFIED(BAD_REQUEST, "AUTH4003", "이메일을 인증 받지 않았습니다."),
    EMAIL_VERIFICATION_CODE_INVALID(BAD_REQUEST, "AUTH4004", "이메일 인증번호가 틀렸습니다."),
    EMAIL_VERIFICATION_EXPIRED(BAD_REQUEST, "AUTH4005", "이메일 인증 유효시간이 만료되었습니다."),

    ACCESS_DENIED(FORBIDDEN, "AUTH4031", "접근 권한이 없습니다."),

    // Mail
    EMAIL_SEND_FAILED(INTERNAL_SERVER_ERROR, "MAIL5001","이메일 전송에 실패했습니다."),

    // User
    USER_NICKNAME_REQUIRED(BAD_REQUEST, "USER4001", "닉네임은 필수 항목입니다."),
    USER_EMAIL_DUPLICATED(BAD_REQUEST, "USER4002", "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(NOT_FOUND, "USER4041", "사용자를 찾을 수 없습니다."),

    // Category
    CATEGORY_NOT_FOUND(NOT_FOUND, "CATEGORY4041", "카테고리 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
