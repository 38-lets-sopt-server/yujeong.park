package org.sopt.domain.post.entity;

import jakarta.persistence.*;
import org.sopt.domain.user.entity.User;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용

    private String title;     // 목록, 상세, 글쓰기 화면 — 제목

    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용

    private String createdAt; // 목록, 상세 화면 — 작성 시각

    @ManyToOne(fetch = FetchType.LAZY)  // User : Post = 1 : N
    @JoinColumn(name = "user_id")       // post 테이블에 user_id FK 컬럼이 생겨요
    private User user;

    protected Post() {}

    public Post(String title, String content, User user, String createdAt) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Long getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }
    public User getUser() { return this.user; }
    public String getCreatedAt() { return this.createdAt; }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}