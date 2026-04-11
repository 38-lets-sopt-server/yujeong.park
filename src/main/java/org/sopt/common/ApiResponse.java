package org.sopt.common;

public class ApiResponse<T> {
    public boolean success; // 성공 여부
    public String message;  // 성공/실패 메시지
    public T data;          // 성공 시 반환할 데이터

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 - 데이터 있음
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // 성공 응답 - 데이터 없음
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}