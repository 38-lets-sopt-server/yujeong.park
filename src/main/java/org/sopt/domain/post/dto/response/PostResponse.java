package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

// 게시글 단건 조회 응답
public record PostResponse(
        Long id,
        String title,
        String content,
        String nickname,
        String createdAt
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