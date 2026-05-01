package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

// 게시글 수정 응답
public record UpdatePostResponse(
        Long id,
        String title,
        String content,
        String nickname,
        String createdAt
) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}