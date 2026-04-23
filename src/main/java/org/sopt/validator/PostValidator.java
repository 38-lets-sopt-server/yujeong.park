package org.sopt.validator;

import org.sopt.exception.PostErrorCode;
import org.sopt.exception.PostException;
import org.springframework.stereotype.Component;

@Component
public class PostValidator {

    private static final int MAX_TITLE_LENGTH = 50;

    // 제목 검증
    public void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new PostException(PostErrorCode.POST_TITLE_REQUIRED);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new PostException(PostErrorCode.POST_TITLE_TOO_LONG);
        }
    }

    // 내용 검증
    public void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new PostException(PostErrorCode.POST_CONTENT_REQUIRED);
        }
    }

    // 제목 + 내용 검증
    public void validate(String title, String content) {
        validateTitle(title);
        validateContent(content);
    }
}
