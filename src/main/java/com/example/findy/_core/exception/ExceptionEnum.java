package com.example.findy._core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "E1", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E2", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E3", "허용되지 않은 HTTP 메서드 요청입니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "E4", "잘못된 게이트웨이 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E5", "접근이 금지되었습니다."),
    TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "E6", "요청 시간이 초과되었습니다."),
    CONFLICT(HttpStatus.CONFLICT, "E7", "요청 데이터가 서버 데이터와 충돌합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E8", "서버에서 알 수 없는 오류가 발생했습니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E9", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
