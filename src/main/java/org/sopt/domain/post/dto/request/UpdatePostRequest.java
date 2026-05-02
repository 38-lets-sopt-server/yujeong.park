package org.sopt.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 게시글 수정 요청
public record UpdatePostRequest(
        @Schema(description = "수정할 게시글 제목 (최대 50자)", example = "수정된 제목")
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
        String title,

        @Schema(description = "수정할 게시글 내용", example = "수정된 내용")
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
}