package org.sopt.exception;

import org.sopt.common.CustomException;

public class PostException extends CustomException {

    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}