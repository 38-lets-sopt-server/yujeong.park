package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

import java.time.LocalDateTime;

// 게시글 작성 응답
public record CreatePostResponse(
        Long id,
        String title,
        String content,
        String nickname,
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