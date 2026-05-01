package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.entity.Post;

// 게시글 리스트 조회 응답
public record PostListResponse(
        Long id,
        String title,
        String content,
        String nickname,
        String createdAt
) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}