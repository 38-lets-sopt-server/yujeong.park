package org.sopt.domain.post.exception;

import org.sopt.global.exception.CustomException;

public class PostException extends CustomException {

    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}