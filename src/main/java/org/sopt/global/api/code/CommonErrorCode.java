package org.sopt.global.api.code;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_002", "잘못된 요청입니다."),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "COMMON_003", "잘못된 경로 변수입니다."),
    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON_004", "잘못된 쿼리 파라미터입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "COMMON_005", "올바르지 않은 JSON 형식입니다."),
    OPTIMISTIC_LOCK_CONFLICT(HttpStatus.CONFLICT, "COMMON_006", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() { return httpStatus; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}