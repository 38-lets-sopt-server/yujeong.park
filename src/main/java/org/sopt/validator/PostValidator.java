package org.sopt.validator;

public class PostValidator {

    // 제목 검증
    public void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
    }

    // 내용 검증
    public void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
    }

    // 제목 + 내용 검증
    public void validate(String title, String content) {
        validateTitle(title);
        validateContent(content);
    }
}
