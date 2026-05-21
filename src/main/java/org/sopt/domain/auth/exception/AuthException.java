package org.sopt.domain.auth.exception;

import org.sopt.global.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}