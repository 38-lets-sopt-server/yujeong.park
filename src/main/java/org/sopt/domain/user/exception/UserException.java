package org.sopt.domain.user.exception;

import org.sopt.global.exception.CustomException;

public class UserException extends CustomException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}