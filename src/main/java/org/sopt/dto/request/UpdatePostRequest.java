package org.sopt.dto.request;

// 게시글 수정 요청 (클라이언트 → 서버)
public class UpdatePostRequest {
    private final String title;
    private final String content;

    public UpdatePostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
}