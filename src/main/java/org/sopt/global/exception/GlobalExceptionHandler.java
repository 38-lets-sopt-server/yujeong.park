package org.sopt.global.exception;

import org.sopt.global.api.CommonResponse;
import org.sopt.global.api.code.CommonErrorCode;
import org.sopt.global.api.code.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException을 상속한 모든 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(CommonResponse.error(e.getErrorCode()));
    }

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("유효성 검증에 실패했습니다.");
                    errors.merge(fieldName, errorMessage, (existing, newMsg) -> existing + ", " + newMsg);
                });
        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getHttpStatus())
                .body(CommonResponse.error(CommonErrorCode.BAD_REQUEST, errors));
    }

    // Path Variable, Query Parameter 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        boolean isPathVariable = e.getParameter().hasParameterAnnotation(PathVariable.class);
        ErrorCode errorCode = isPathVariable
                ? CommonErrorCode.INVALID_PATH_VARIABLE
                : CommonErrorCode.INVALID_QUERY_PARAMETER;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.error(errorCode));
    }

    // JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(CommonErrorCode.INVALID_JSON.getHttpStatus())
                .body(CommonResponse.error(CommonErrorCode.INVALID_JSON));
    }

    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(CommonResponse.error(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}