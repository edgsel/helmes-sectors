package com.helmes.sectorsapi.exception;

import lombok.Getter;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;

@Getter
public class BadCredentialsException extends RuntimeException {

    private final ErrorCode code;

    public BadCredentialsException(String description) {
        super(description);
        this.code = INVALID_CREDENTIALS;
    }
}
