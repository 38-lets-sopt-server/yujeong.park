package org.sopt.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.auth.dto.TokenResult;

public record TokenResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken
) {
    public static TokenResponse from(TokenResult result) {
        return new TokenResponse(result.accessToken());
    }
}