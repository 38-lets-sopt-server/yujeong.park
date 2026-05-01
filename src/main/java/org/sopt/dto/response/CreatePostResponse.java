package org.sopt.dto.response;

import org.sopt.domain.Post;

// 게시글 작성 응답
public record CreatePostResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt
) {
    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}