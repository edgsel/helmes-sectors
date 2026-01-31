package com.helmes.sectorsapi.exception;

import lombok.Getter;

import static com.helmes.sectorsapi.exception.ErrorCode.USER_EXISTS_ERROR;

@Getter
public class UserExistsException extends RuntimeException {

    private final String code;

    public UserExistsException(String description) {
        super(description);
        this.code = USER_EXISTS_ERROR.name();
    }
}
