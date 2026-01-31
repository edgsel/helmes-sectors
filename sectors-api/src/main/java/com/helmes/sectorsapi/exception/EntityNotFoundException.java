package com.helmes.sectorsapi.exception;

import lombok.Getter;

@Getter
public abstract class EntityNotFoundException extends RuntimeException {

    private final String code;

    protected EntityNotFoundException(String description, String code) {
        super(description);
        this.code = code;
    }
}
