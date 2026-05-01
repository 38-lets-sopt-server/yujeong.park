package org.sopt.domain.user.exception;

import org.sopt.global.CustomException;

public class UserException extends CustomException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}