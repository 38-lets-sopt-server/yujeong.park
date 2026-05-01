package org.sopt.dto.response;

import org.sopt.domain.Post;

// 게시글 리스트 조회 응답
public record PostListResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt
) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}