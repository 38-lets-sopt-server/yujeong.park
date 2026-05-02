package org.sopt.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.entity.Post;

import java.time.LocalDateTime;

// 게시글 작성 응답
public record CreatePostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 제목", example = "오늘 학식 뭐임")
        String title,

        @Schema(description = "게시글 내용", example = "돈까스래")
        String content,

        @Schema(description = "작성자 닉네임", example = "익명")
        String nickname,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt
) {
    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}