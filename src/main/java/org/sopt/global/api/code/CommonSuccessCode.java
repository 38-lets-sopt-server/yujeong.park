package org.sopt.global.api.code;

import org.springframework.http.HttpStatus;

public enum CommonSuccessCode implements SuccessCode {

    OK(HttpStatus.OK, "COMMON_200", "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED, "COMMON_201", "생성이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommonSuccessCode(HttpStatus httpStatus, String code, String message) {
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