package org.sopt.dto.response;

import org.sopt.domain.Post;

// 게시글 조회 응답
public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt
) {

    // Post 객체를 PostResponse로 변환하는 정적 팩토리 메서드
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}