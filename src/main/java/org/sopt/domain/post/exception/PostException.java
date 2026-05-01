package org.sopt.domain.post.exception;

import org.sopt.global.CustomException;

public class PostException extends CustomException {

    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}