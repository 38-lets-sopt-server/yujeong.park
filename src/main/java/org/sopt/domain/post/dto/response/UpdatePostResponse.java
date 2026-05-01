package org.sopt.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.entity.Post;

import java.time.LocalDateTime;

// 게시글 수정 응답
public record UpdatePostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 제목", example = "수정된 제목")
        String title,

        @Schema(description = "게시글 내용", example = "수정된 내용")
        String content,

        @Schema(description = "작성자 닉네임", example = "익명")
        String nickname,

        @Schema(description = "좋아요 수", example = "5")
        int likeCount,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt
) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getLikeCount(),
                post.getCreatedAt()
        );
    }
}