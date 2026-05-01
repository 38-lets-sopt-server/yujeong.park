package org.sopt.domain.post.exception;

import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_001", "해당 게시글이 존재하지 않습니다."),
    POST_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "POST_002", "제목은 필수입니다."),
    POST_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "POST_003", "제목은 50자 이하여야 합니다."),
    POST_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "POST_004", "내용은 필수입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    PostErrorCode(HttpStatus httpStatus, String code, String message) {
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