package org.sopt.domain.like.exception;

import org.sopt.global.exception.CustomException;

public class LikeException extends CustomException {

    public LikeException(LikeErrorCode errorCode) {
        super(errorCode);
    }
}