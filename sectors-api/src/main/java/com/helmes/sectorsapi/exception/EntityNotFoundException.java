package com.helmes.sectorsapi.exception;

import lombok.Getter;

@Getter
public abstract class EntityNotFoundException extends RuntimeException {

    private final ErrorCode code;

    protected EntityNotFoundException(String description, ErrorCode code) {
        super(description);
        this.code = code;
    }
}
