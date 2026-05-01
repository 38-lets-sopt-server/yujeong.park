package org.sopt.domain.post.dto.request;

// 게시글 작성 요청
public record CreatePostRequest(
        Long userId,
        String title,
        String content,
        String author
) {
}