package org.sopt.dto.response;

import org.sopt.domain.Post;

// 게시글 수정 응답
public record UpdatePostResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt
) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}