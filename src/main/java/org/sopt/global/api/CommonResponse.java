package org.sopt.global.api;

import org.sopt.global.api.code.CommonSuccessCode;
import org.sopt.global.api.code.ErrorCode;

public record CommonResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    // 200 OK, 데이터 있음
    public static <T> CommonResponse<T> ok(String message, T data) {
        return new CommonResponse<>(true, CommonSuccessCode.OK.getCode(), message, data);
    }

    // 200 OK, 데이터 없음
    public static <T> CommonResponse<T> ok(String message) {
        return new CommonResponse<>(true, CommonSuccessCode.OK.getCode(), message, null);
    }

    // 201 Created, 데이터 있음
    public static <T> CommonResponse<T> created(String message, T data) {
        return new CommonResponse<>(true, CommonSuccessCode.CREATED.getCode(), message, data);
    }

    // 201 Created, 데이터 없음
    public static <T> CommonResponse<T> created(String message) {
        return new CommonResponse<>(true, CommonSuccessCode.CREATED.getCode(), message, null);
    }

    public static <T> CommonResponse<T> error(ErrorCode errorCode) {
        return new CommonResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }
}