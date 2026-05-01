package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

import java.time.LocalDateTime;

// 게시글 단건 조회 응답
public record PostResponse(
        Long id,
        String title,
        String content,
        String nickname,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}