package org.sopt.global;

import org.sopt.global.code.CommonSuccessCode;
import org.sopt.global.code.ErrorCode;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    // 200 OK, 데이터 있음
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, CommonSuccessCode.OK.getCode(), message, data);
    }

    // 200 OK, 데이터 없음
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(true, CommonSuccessCode.OK.getCode(), message, null);
    }

    // 201 Created, 데이터 있음
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, CommonSuccessCode.CREATED.getCode(), message, data);
    }

    // 201 Created, 데이터 없음
    public static <T> ApiResponse<T> created(String message) {
        return new ApiResponse<>(true, CommonSuccessCode.CREATED.getCode(), message, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }
}