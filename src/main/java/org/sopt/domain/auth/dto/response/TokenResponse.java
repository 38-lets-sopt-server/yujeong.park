package org.sopt.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
        String refreshToken
) {
    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}