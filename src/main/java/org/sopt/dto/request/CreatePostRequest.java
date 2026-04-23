package org.sopt.dto.request;

// 게시글 작성 요청
public record CreatePostRequest(
        String title,
        String content,
        String author
) {

}