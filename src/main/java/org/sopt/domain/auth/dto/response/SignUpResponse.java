package org.sopt.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.user.entity.User;

public record SignUpResponse(
        @Schema(description = "유저 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "test@test.com")
        String email,

        @Schema(description = "닉네임", example = "익명")
        String nickname
) {
    public static SignUpResponse from(User user) {
        return new SignUpResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}