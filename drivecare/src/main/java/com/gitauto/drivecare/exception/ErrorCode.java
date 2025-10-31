package com.gitauto.drivecare.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 실패"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 거부"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스 없음"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류"),
    INVALIE_USERID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호"),
    INVALID_RESERVATION_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 정보");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String msg) {
        this.status = status;
        this.defaultMessage = msg;
    }
}
